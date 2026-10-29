package com.qr.custom_qr_authentication.controllers;

import com.qr.custom_qr_authentication.dto.VerifyRequest;
import com.qr.custom_qr_authentication.entity.UserEntity;
import com.qr.custom_qr_authentication.mappers.UserMapper;
import com.qr.custom_qr_authentication.service.UserService;
import com.qr.custom_qr_authentication.service.database.IDatasource;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;


/**
 *  - If je log the first time with the QR code.
 *  You can the after the code change of this QR code login
 *
 *  - if generate a new QR code, then you must change the name in the windows authenticator
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{version}/2fa")
@Tag(name= "Twofactor check")
public class TwoFactorController {

    private final SecretGenerator secretGenerator;
    private final QrDataFactory qrDataFactory;
    private final QrGenerator qrGenerator;

    private final IDatasource dataRs;

    private final UserMapper usermap;
    private final UserService userSv; // serverse dat get the user from the springboor system
    private final CodeVerifier verifier;

    @Value("${twofactor.app.name}")
    String twofactorAppName;

    @Operation(summary = "Setup the 2 way authentication", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(
                            schema = @Schema(implementation = Object.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request, missing required fields."),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "406", description = "Not Acceptable"),
            @ApiResponse(responseCode = "415", description = "Bad Request, missing required fields."),
            @ApiResponse(responseCode = "429", description = "Retry After Some Time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "default")
    })
    @GetMapping("/setup")
    public ResponseEntity<Object> setupDevice(Principal principal) throws QrGenerationException {

        // 1. Secure: Identificeer gebruiker via Spring Security sessie/JWT
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niet ingelogd.");
        }

        var findUser = dataRs.getUserByUsername(principal.getName());
        if (findUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gebruiker niet gevonden.");
        }

        UserEntity user = findUser.get();

        // 2. Instellen van de issuer (app-naam)
        String issuer = this.twofactorAppName;

        // 3. Secret ophalen (tijdelijk) of een nieuwe genereren en opslaan
        String secret;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getTempSecret())) {
            secret = user.getTempSecret();
        } else {
            secret = secretGenerator.generate();
            user.setTempSecret(secret);
            userSv.updateUser(user.getId(), usermap.toDto(user));
        }

        // 4. Zorg dat het label exact de structuur "Issuer:email" volgt
        String emailAddress = "Onbekend";
        if (user.getPerson() != null && user.getPerson().getEmail() != null) {
            emailAddress = user.getPerson().getEmail().replaceAll("\\s+", "");
        }

        String cleanLabel = issuer + ":" + emailAddress;

        // 5. Bouw de QR data met de juiste gegevens
        QrData data = qrDataFactory.newBuilder()
                .label(cleanLabel)
                .secret(secret)
                .issuer(issuer) // Zorgt ervoor dat Authenticator apps de metadata correct tonen
                .build();

        String qrCodeUri = Utils.getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );

        // Retourneert exact ResponseEntity<Object>
        return ResponseEntity.ok(Collections.singletonMap("qrCode", qrCodeUri));
    }

    @Operation(summary = "Verify the 2 way authantication code",responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(
                            schema = @Schema(implementation = Object.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request, missing required fields."
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not Found"
            ),
            @ApiResponse(responseCode = "406",
                    description = "Not Acceptable"
            ),
            @ApiResponse(responseCode = "415",
                    description = "Bad Request, missing required fields."
            ),
            @ApiResponse(responseCode = "429",
                    description = "Retry After Some Time"
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error"
            ),
            @ApiResponse(
                    responseCode = "default"
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<Object> verify(Principal principal,@RequestBody VerifyRequest request) {

        // SECURITY FIX: Identificeer de gebruiker op basis van de actieve sessie (Principal),
        // niet op basis van een ID die vanuit de frontend meegestuurd wordt.

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niet ingelogd.");
        }



        var findUser = dataRs.getUserByUsername(principal.getName());
        if (findUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gebruiker niet gevonden.");
        }

        UserEntity user = findUser.get();

        // 2. Bepaal welk geheim we moeten valideren
        String secretToValidate = null;

        if (StringUtils.isNotBlank(user.getTempSecret())) {
            // Prioriteit 1: Er is een lopende registratie/wijziging (tempSecret)
            secretToValidate = user.getTempSecret();
        } else if (StringUtils.isNotBlank(user.getSecret().orElse(""))) {
            // Prioriteit 2: Geen tijdelijk geheim, controleer tegen het reeds actieve geheim
            secretToValidate = user.getSecret().orElse("");
        }

        if (secretToValidate == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Geen 2FA configuratie gevonden voor deze gebruiker.");
        }

        // 3. Valideer de pincode tegen het gekozen geheim
        if (verifier.isValidCode(secretToValidate, request.getCode())) {

            // 4. Als de code geldig was EN het kwam uit de 'tempSecret', activeer deze dan definitief
            if (StringUtils.isNotBlank(user.getTempSecret())) {
                user.setSecret(user.getTempSecret());
                user.setTempSecret(null); // Wis de tijdelijke buffer
                user.setMfaEnabled(true);
                userSv.updateUser(user.getId(),usermap.toDto(user));
            }

            return ResponseEntity.ok(Collections.singletonMap("status", "2FA succesvol geverifieerd"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ongeldige code. Probeer het opnieuw.");
    }

}

package com.qr.custom_qr_authentication.config.twoway;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuratieklasse voor Two-Factor Authentication (TOTP).
 * Initialiseert de benodigde beans voor de 'dev.samstevens.totp' bibliotheek,
 * zoals secret generatie, QR-code generatie en code verificatie.
 */
@Configuration
public class TotpConfig {

    @Bean
    public SecretGenerator secretGenerator() {
        return new DefaultSecretGenerator();
    }

    @Bean
    public QrDataFactory qrDataFactory() {
        // SHA1 is de standaard voor de meeste 2FA apps
        // 6 is de standaard code lengte
        // 30 is de standaard tijd periode in seconden
        return new QrDataFactory(HashingAlgorithm.SHA1, 6, 30);
    }
    @Bean
    public QrGenerator qrGenerator() {
        return new ZxingPngQrGenerator();
    }

    @Bean
    public CodeVerifier codeVerifier() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        return new DefaultCodeVerifier(codeGenerator, timeProvider);
    }
}

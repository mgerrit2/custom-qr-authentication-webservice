package com.qr.custom_qr_authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;


@Builder
@Jacksonized
@Schema(
        name = "JWTDto",
        description = """
                Hardened Data Transfer Object for JWT configurations and claims. 
                Validated to prevent injection and buffer overflow attacks.
                """
)
public record JWTDto(

        @Schema(
                description = "Internal configuration ID",
                minimum = "1",
                maximum = "9223372036854775807",
                example = "1024"
        )
        @Min(1)
        @Max(9223372036854775807L)
        Long id,

        @Schema(
                description = """
                        Base64 encoded cryptographic string used to sign the JWT. Masked in logs.
                        This ensures that the token cannot be tampered with by a client after it is issued.
                        """,
                minLength = 32, maxLength = 256,
                pattern = "^[A-Za-z0-9+/=_-]+$",
                example = "dGhpcy1pcy1hLXNlY3VyZS0yNTYtYml0LXNlY3JldC1rZXk="
        )
        @Size(min = 32, max = 2048)
        @Pattern(regexp = "^[A-Za-z0-9+/=_-]+$")
        String secretKey,

        @Schema(description = "Token expiration time in seconds (max 10 years).",
                minimum = "1", maximum = "315360000",
                example = "3600")
        @Min(1) @Max(315360000L)
        Long expiration,

        @Schema(
                description = "Target audience claim URL or identifier.",
                maxLength = 255, pattern = "^[A-Za-z0-9:/.\\-_]+$",
                example = "https://api.gscience.com"
        )
        @Size(max = 255)
        @Pattern(regexp = "^[A-Za-z0-9:/.\\-_]+$")
        String audience,

        @Schema(
                description = """
                       In Identity Providers (like Microsoft Entra ID / Azure AD): oid is the Object Identifier claim.
                       It is a unique, immutable GUID assigned to a specific user or service principal.
                       Even if a user changes their email or username, their oid remains exactly the same.
                       
                       In General Networking/Cryptography: It stands for Object Identifier, a standardized,
                       dot-separated string of numbers (e.g., 1.2.840.113549...) used to uniquely name objects,
                       cryptographic algorithms, or certificate fields globally.
                        """,
                maxLength = 100,
                pattern = "^[A-Za-z0-9_\\-]+$",
                example = "k-2026-v1"
        )
        @Size(max = 100)
        @Pattern(regexp = "^[A-Za-z0-9_\\-]+$")
        String keyId,

        @Schema(
                description = """
                       Object ID of Unique Object Identifier
                        - Het is een unieke code (vaak een UUID of GUID, zoals 123e4567-e89b-12d3-a456-426614174000) die specifiek één object aanduidt.
                        - Binnen identiteitsproviders zoals Microsoft Entra ID (voorheen Azure AD) of Okta is de oid de onveranderlijke, unieke sleutel van een gebruiker,
                          een service principal of een groep in de database. Zelfs als een gebruiker zijn e-mailadres of naam wijzigt, blijft de oid altijd exact hetzelfde.
                        JWT
                        - Een JWT bestaat uit drie delen, gescheiden door punten: een Header,
                          een Payload (de data) en een Signature (de digitale handtekening).
                        - Nadat een gebruiker succesvol is ingelogd, genereert de server een JWT.
                          De app stuurt dit token daarna bij elk verzoek mee om te bewijzen wie de gebruiker is.
                        """,
                maxLength = 100, format = "uuid",
                pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @Size(max = 100)
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        String oid,

        @Schema(
                description = """
                        The "iss" (Issuer) claim identifies the principal that issued the JWT.
                        <p>
                        In OAuth 2.0 and OpenID Connect configurations, this value is typically a
                        case-sensitive string containing a URL or identifier for the authorization server.
                        </p>
                        
                        @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1">RFC 7519 Section 4.1.1 (Issuer)</a>
                        """,
                maxLength = 100, pattern = "^https?://[A-Za-z0-9:.\\-_/]+$",
                example = "https://login.microsoftonline.com/v2.0")
        @Size(max = 100)
        @Pattern(regexp = "^https?://[A-Za-z0-9:.\\-_/]+$")
        String iss,

        @Schema(description = "OAuth2 authorized party (azp) client identification mapping.",
                maxLength = 255, pattern = "^[A-Za-z0-9_\\-\\s.]+$", example = "gscience-web-app"
        )
        @Size(max = 255)
        @Pattern(regexp = "^[A-Za-z0-9_\\-\\s.]+$")
        String authorizedParty,

        @Schema(
                description = "Authentication Context Class Reference routing identifier.",
                maxLength = 100, pattern = "^[A-Za-z0-9_\\-]+$", example = "0"
        )
        @Size(max = 100)
        @Pattern(regexp = "^[A-Za-z0-9_\\-]+$")
        String azpacrValue,

        @Schema(
                description = "Sanitized historical routing context payload trace info.",
                maxLength = 1000, example = "origin=192.168.1.1;route=/auth"
        )
        @Size(max = 1000)
        String requestHistory,

        @Schema(
                description = """
                        <pre>
                        Crucial for multi-tenant architectures. It represents the specific corporate directory,
                        sub-organization, or database partition to which the authenticating user belongs.
                        </pre>
                        """,
                maxLength = 100, format = "uuid",
                pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                example = "987f6543-e21b-73d4-b654-987614173999"
        )
        @Size(max = 100)
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        String tenantId,

        @Schema(
                description = """
                <pre>
                Tracks the layout version of the token payload. If your token structure changes in a future deployment,
                your backend can use this to parse older vs. newer tokens correctly.
                </pre>
                """,
                maxLength = 50,
                pattern = "^[0-9.]+$",
                example = "2.0"
        )
        @Size(max = 50)
        @Pattern(regexp = "^[0-9.]+$")
        String tokenVersion,

        /**
         * <pre>
         * An internal or extended claim (often prefixed with xms_ by Microsoft)
         * typically used to convey specific deployment features,
         * conditional access policies, or routing hints.
         * </pre>
         */
        @Schema(
                description = """
                <pre>
                An internal or extended claim (often prefixed with xms_ by Microsoft)
                typically used to convey specific deployment features,
                conditional access policies, or routing hints.
                </pre>
                """,
                maxLength = 255,
                pattern = "^[A-Za-z0-9_\\-]+$",
                example = "extended-feature-flag"
        )
        @Size(max = 255)
        @Pattern(regexp = "^[A-Za-z0-9_\\-]+$")
        String xmsFtd,

        @Schema(
                description = "Flattened relational foreign reference to the parent user entity identifier.",
                minimum = "1",
                maximum = "9223372036854775807",
                example = "58403"
        )
        @Min(1) @Max(9223372036854775807L)
        Long userId, // Flattened reference to the associated UserEntity ID


        @Schema(
                description = "Flags whether the company tenant is fully operational.",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Active status must be specified")
                Boolean active



) implements Serializable {
    // Simply adding the implements clause solves the crash!
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Genereert een multi-line string-representatie van het token object via reflectie.
     * Elk veld wordt overzichtelijk op een nieuwe regel geprint in de logs.
     *
     * @return Multi-line geformatteerde string van de token metagegevens.
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .toString();
    }
}

package com.qr.custom_qr_authentication.entity.secutity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

//@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
/*
@SQLDelete(sql = "UPDATE security_schema.jwt SET active = false WHERE id = ? AND record_version = ?")
@Entity
@Table(name = "jwt",schema = "security_schema")
 */
public class JWTEntity {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     */
    private Long id;

    /**
     * <pre>
     * The cryptographic string used to sign the JWT.
     * This ensures that the token cannot be tampered with by a client after it is issued.
     * </pre>
     */
    /*
    @Convert(converter = KeyEncryptionConverter.class)
    @Column(name = "secret_key", length = 2048)
     */
    private String secretKey;

    /**
     * <pre>
     * The lifespan of the token in milliseconds. Once this duration passes,
     * the token is rejected by the system, forcing the user to re-authenticate or use a refresh token.
     * </pre>
     */
    //@Column(name = "expiration")
    private Long expiration;

    /**
     * <pre>
     * Identifies the specific recipients that the JWT is intended for (e.g.,
     * the API URL or Client ID of your resource server).
     * If a service receives a token where the aud does not match its own identifier, it must reject it.
     * </pre>
     */
    //@Column(name = "audience", length = 255)
    private String audience;

    /**
     * In Identity Providers (like Microsoft Entra ID / Azure AD): oid is the Object Identifier claim.
     * It is a unique, immutable GUID assigned to a specific user or service principal.
     * Even if a user changes their email or username, their oid remains exactly the same.
     *
     * In General Networking/Cryptography: It stands for Object Identifier, a standardized,
     * dot-separated string of numbers (e.g., 1.2.840.113549...) used to uniquely name objects,
     * cryptographic algorithms, or certificate fields globally.
     */
    //@Column(name = "oid", length = 100)
    private String oid;

    /**
     * The "iss" (Issuer) claim identifies the principal that issued the JWT.
     * <p>
     * In OAuth 2.0 and OpenID Connect configurations, this value is typically a
     * case-sensitive string containing a URL or identifier for the authorization server.
     * </p>
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1">RFC 7519 Section 4.1.1 (Issuer)</a>
     */
    //@Column(name = "iss", length = 100)
    private String iss;


    /**
     * <pre>
     * A hint placed in the JWT header that tells the validating application
     * which specific public/private key pair was used to sign the token.
     * This is crucial for environments that rotate signing keys regularly.
     * </pre>
     */
    //@Column(name = "key_id", length = 2050)
    private String keyId;

    //@Column(name = "authorized_party", length = 255)
    private String authorizedParty;


    /**
     * The configured value representing the custom Authorized Party Access Control Rule (azpacr).
     * This value is embedded into JWT payloads to validate specific authentication context tiers
     * across distributed services.
     *
     * <p><strong>42Crunch Security Context:</strong> Externally managed via configuration profiles.
     * Ensure that production deployment overrides this property using secure secrets management
     * (e.g., Vault, Kubernetes Secrets) to prevent token template manipulation or identity spoofing.</p>
     *
     * @see <a href="https://42crunch.com/blog/">API Security Context Management</a>
     */
    //@Column(name = "azpacr_value", length = 100)
    private String azpacrValue;

    //@Lob // Or @Column(name = "request_history", columnDefinition = "TEXT")
    //@Column(name = "request_history")
    private String requestHistory;


    /**
     * <pre>
     * Crucial for multi-tenant architectures. It represents the specific corporate directory,
     * sub-organization, or database partition to which the authenticating user belongs.
     * </pre>
     */
    //@Column(name = "tenant_id", length = 100)
    private String tenantId;

    /**
     * <pre>
     * Tracks the layout version of the token payload. If your token structure changes in a future deployment,
     * your backend can use this to parse older vs. newer tokens correctly.
     * </pre>
     */
    @Builder.Default
    //@Column(name = "token_version", length = 50)
    private String tokenVersion = "v1.0";

    /**
     * <pre>
     * An internal or extended claim (often prefixed with xms_ by Microsoft)
     * typically used to convey specific deployment features,
     * conditional access policies, or routing hints.
     * </pre>
     */
    //@Column(name = "xms_ftd", length = 255)
    private String xmsFtd;

    /*
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
     */
    private OffsetDateTime createdAt;

    /*
    @CreatedBy
    @Column(name = "created_by", length = 50, nullable = true, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy; // Tracks who soft-deleted or edited the record


    //region optional getters
    public Optional<String> getSecretKey(){
        return Optional.ofNullable(this.secretKey);
    }
    public Optional<Long> getExpiration(){
        return Optional.ofNullable(this.expiration);
    }
    public Optional<String> getAudience(){
        return Optional.ofNullable(this.audience);
    }
    public Optional<String> getKeyId(){
        return Optional.ofNullable(this.keyId);
    }
    public Optional<String> getAuthorizedParty(){
        return Optional.ofNullable(this.authorizedParty);
    }
    public Optional<String> getTenantId(){
        return Optional.ofNullable(this.tenantId);
    }
    public Optional<String> getTokenVersion(){
        return Optional.ofNullable(this.tokenVersion);
    }
    public Optional<String> getXmsFtd(){
        return Optional.ofNullable(this.xmsFtd);
    }
    public Optional<OffsetDateTime> getCreatedAt(){
        return Optional.of(this.createdAt);
    }
    public Optional<String> getCreatedBy(){
        return Optional.of(this.createdBy);
    }
    public Optional<OffsetDateTime> getUpdatedAt(){
        return Optional.of(this.updatedAt);
    }
    public Optional<String> getUpdatedBy(){
        return Optional.of(this.lastModifiedBy);
    }
    public Optional<UserEntity> getUser(){
        return Optional.of(this.user);
    }
    public Optional<String> getRequestHistory(){return Optional.of(this.requestHistory);}

    //endregion

    @Column(name = "active")
    private boolean active;

    @Builder.Default
    @Version
    @Column(name = "record_version")
    private Long version = 0L;

    // Add this field inside JWTEntity.java

    //region Description
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;
    //endregion

    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JWTEntity jWTEntity)) return false;
        return Objects.equals(this.id, jWTEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion

    /**
     * this will print the class fields with newline for each field
     *
     * @return
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .setExcludeFieldNames("user")
                .toString();
    }

}

package com.qr.custom_qr_authentication.security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Log4j2
@Service
public class JwtService {

    @Value("${APPLICATION_SECURITY_JWT_SECRET_KEY}")
    private String secretKey;

    /**
     * Extracts the username (subject) from the JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks if the token belongs to the given username and is not expired.
     */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

     /**
     * Checks if the JWT token has expired using modern java.time (Instant).
     */
    private boolean isTokenExpired(String token) {
        Instant expirationInstant = extractExpiration(token);
        return expirationInstant.isBefore(Instant.now());
    }

    /**
     * Extracts the expiration date from the token as a modern java.time.Instant.
     */
    private Instant extractExpiration(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        // Convert legacy java.util.Date to modern java.time.Instant
        return expirationDate != null ? expirationDate.toInstant() : Instant.now().minusSeconds(1); // fallback safely if null
    }

    /**
     * Parses all claims from the token using the secret key.
     */
    private Claims extractAllClaims(String token) {
        try {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException e) {
            // Log the specific reason if needed (e.g., expired, invalid signature, malformed)
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }

    /**
     * Converts the secret string into a cryptographic SecretKey.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes =   io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Converts an array of bytes into a human-readable hexadecimal string.
     * This is particularly useful for debugging and logging binary data,
     * such as cryptographic signing keys.
     *
     * @param bytes the raw byte array to convert
     * @return a 2-digit uppercase hexadecimal string representation of the bytes
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        // Iterate through each byte in the provided array
        for (byte b : bytes) {
            // Format the byte as a 2-character uppercase hex value (e.g., 0A, FF)
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }
}

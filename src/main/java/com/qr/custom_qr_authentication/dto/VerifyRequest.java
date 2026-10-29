package com.qr.custom_qr_authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode via Lombok
@NoArgsConstructor // Generates a no-args constructor required for JSON deserialization
@AllArgsConstructor // Generates a constructor with all fields
public class VerifyRequest {
    private String code;
}

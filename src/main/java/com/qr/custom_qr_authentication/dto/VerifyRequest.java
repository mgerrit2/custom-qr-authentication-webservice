package com.qr.custom_qr_authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRequest {
    @NotBlank(message = "Code cannot be blank")
    @Size(min = 6, max = 6, message = "Code must be exactly 6 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Code must contain only numeric digits")
    private String code;
}

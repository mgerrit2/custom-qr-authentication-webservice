package com.qr.custom_qr_authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response payload for 2FA setup")
public class TwoFactorSetupResponseDto {
    @Schema(description = "Base64 QR code data URI", example = "data:image/png;base64,...")
    private String qrCode;
}
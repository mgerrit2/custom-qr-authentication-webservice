package com.qr.custom_qr_authentication.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a user role mapping.")
public record UserRoleDTO(
        @Schema(
                description = "Unique identifier of the user role mapping",
                example = "101",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Name of the assigned role", example = "ROLE_USER", maxLength = 50)
        @NotBlank(message = "Role name cannot be blank")
        @Size(max = 50, message = "Role name must not exceed 50 characters")
        String role,
        Long userID
) {
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

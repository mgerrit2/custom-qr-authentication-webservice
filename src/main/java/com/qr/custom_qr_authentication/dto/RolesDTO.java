package com.qr.custom_qr_authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL) // Omit null fields
public record RolesDTO(

        @Schema(
                description = "Unique identifier of the role",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Indicates whether this is the application default role", example = "false")
        @NotNull(message = "Default role flag cannot be null")
        boolean defaultRole,

        @Schema(description = "Unique name of the role", example = "ROLE_ADMIN", maxLength = 50)
        @NotBlank(message = "Role name cannot be blank")
        @Size(max = 50, message = "Role name must not exceed 50 characters")
        String name
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

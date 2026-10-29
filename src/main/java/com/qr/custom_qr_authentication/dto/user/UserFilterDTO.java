package com.qr.custom_qr_authentication.dto.user;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(
        name = "UserFilterDTO",
        description = "Filter criteria schema for searching users. All fields are optional but strictly structured."
)
public class UserFilterDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Filter by user cdsid.", maxLength = 8, pattern = "^[a-zA-Z0-9 \\.,_\\-&'@#]*$", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Pattern(regexp = "^[a-zA-Z0-9 \\.,_\\-&'@#]*$", message = "Filter name contains invalid characters")
    @Size(max = 8, message = "Filter cdsid must not exceed 8 characters")
    private String cdsid;

    @Schema(description = "Filter by active operational status.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean active;

    @Schema(
            description = "Filter by unique corporate person ID.",
            minimum = "1",
            maximum = "999999999",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Min(value = 1, message = "Person ID must be greater than 0")
    @Max(value = 999999999, message = "Person ID exceeds maximum allowed limit")
    private Long personId;

    @Schema(
            description = "Filter by assigned user roles.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ArraySchema(maxItems = 50) // <--- This correctly maps maxItems to OpenAPI
    @Size(max = 50, message = "Cannot filter by more than 50 roles at once")
    private List<@NotBlank @Size(max = 30, message = "Role name is too long")String> roles;

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

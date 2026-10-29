package com.qr.custom_qr_authentication.dto.user;
import com.qr.custom_qr_authentication.dto.RolesDTO;
import com.qr.custom_qr_authentication.security.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserResponceDTO {

    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long id;

    @Size(min = 1,max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "CDSID must contain only letters and numbers.")
    private String cdsid;

    /** * This field, despite the name, holds the plain-text password from the user
     * before it is hashed in the service layer.
     */
    @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Password is required.")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Complexity requirement not met.",
            groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class}
    )
    private String hashPassword;

    private boolean active;

    private OffsetDateTime createdAt;

    private String createdBy;

    private OffsetDateTime updatedAt;

    private String updatedBy;

    private Long personId;

    private List<RolesDTO> roles;

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

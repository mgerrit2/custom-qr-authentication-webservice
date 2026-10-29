package com.qr.custom_qr_authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        Long id,
        boolean defaultRole,
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

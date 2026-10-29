package com.qr.custom_qr_authentication.mappers;


import com.qr.custom_qr_authentication.dto.JWTDto;
import com.qr.custom_qr_authentication.dto.RolesDTO;
import com.qr.custom_qr_authentication.dto.user.UserDTO;
import com.qr.custom_qr_authentication.dto.user.UserResponceDTO;
import com.qr.custom_qr_authentication.entity.CompanyEntity;
import com.qr.custom_qr_authentication.entity.UserEntity;
import com.qr.custom_qr_authentication.entity.UserRolesEntity;
import com.qr.custom_qr_authentication.entity.secutity.JWTEntity;
import org.mapstruct.*;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR // Goede praktijk: geeft fout bij vergeten velden
)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cdsid", source = "name") // Een Principal heeft een getName() methode die de username bevat
    @Mapping(target = "secret", ignore = true)
    @Mapping(target = "tempSecret", ignore = true)
    @Mapping(target = "hashPassword", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "mfaEnabled", ignore = true)
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "oid", ignore = true)
    @Mapping(target = "companyIds", ignore = true)
    @Mapping(target = "jwtConfiguration", ignore = true)
    UserDTO pricipalToDto(Principal principal);



   // @Mapping(target = "roles", ignore = true) //Managing collections usually requires custom logic (like your addRole helper) to maintain bidirectional integrity.
    @Mapping(target = "version", ignore = true) // JPA beheert de versie zelf
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "userSid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "companies", ignore = true)
    UserEntity toEntity(UserDTO dto);


    //region to DTO mapper
    @Mapping(source = "person.id", target = "personId")
    @Mapping(target = "companyIds", source = "companies")
    @Mapping(target = "jwtConfiguration.userId", source = "id")
    UserDTO toDto(UserEntity entity);

    List<UserDTO> toDtoList(List<UserEntity> entities);

    // 2. Add this nested mapping method so MapStruct knows how to bridge JWTDto -> JWTEntity
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    //@Mapping(target = "user", ignore = true) // Ignores back-reference link to avoid recursion loops
    /*
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    */
    JWTEntity jwtDtoToJwtEntity(JWTDto dto);

    /**
     * Helper to transfur UserRolesEntity to RolesDTO
     */
    @Mapping(target = "name", source = "role")
    @Mapping(target = "defaultRole", ignore = true)
    RolesDTO toRolesDto(UserRolesEntity userRolesEntity);
    //endregion


    @Mapping(target = "updatedBy", source = "lastModifiedBy")
   @Mapping(source = "person.id", target = "personId")
    UserResponceDTO toResponseDto(UserEntity entity);

   /**
    * Maps a list of entities to a list of Response DTOs
    */
   List<UserResponceDTO> toResponseDtoList(List<UserEntity> entities);


    default Long mapCompanyToId(CompanyEntity company) {
        return company != null ? company.getId() : null;
    }


    // --- Helper methoden om Optionals uit te pakken ---

    default <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }

    // Specifieke overload voor String indien nodig
    default String mapString(Optional<String> value) {
        return value.orElse(null);
    }

    // Specifieke overload voor OffsetDateTime
    default OffsetDateTime mapDateTime(Optional<OffsetDateTime> value) {
        return value.orElse(null);
    }


}

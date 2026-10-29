package com.qr.custom_qr_authentication.service;


import com.qr.custom_qr_authentication.dto.user.UserDTO;
import com.qr.custom_qr_authentication.dto.user.UserFilterDTO;
import com.qr.custom_qr_authentication.dto.user.UserResponceDTO;
import com.qr.custom_qr_authentication.entity.UserEntity;
import com.qr.custom_qr_authentication.mappers.UserMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;


@Log4j2
@RequiredArgsConstructor
@CacheConfig(cacheNames = UserService.CACHE_NAME)
@Service
public class UserService {

    public static final String NOT_FOUND_SUFFIX = " not found.";


    public static final String CACHE_NAME = "users";
    public static final String CACHE_PAGES = "users_pages";


    private final UserMapper userMap;
    //private final PasswordEncoder passwordEncoder; // Voor veilige opslag


    public Optional<UserEntity> getUserByUsername(String cdsid) {

        return Optional.empty();
        //return userRs.getUserByUsername(cdsid);
    }


    public UserDTO createUser(UserDTO userDTO) {

        return new UserDTO();
    }


    public List<UserDTO> getAllUsers(
            @Max(value = Integer.MAX_VALUE,message = "Page must be less than or equal to "+ Integer.MAX_VALUE)
            @Min(value = 0,message = "Page must be greather than or equal to 0")
            int page,
            @Max(value = Integer.MAX_VALUE,message = "Page must be less than or equal to "+ Integer.MAX_VALUE)
            @Min(value = 1,message = "Size must be greather than or equal to 1")
            int size
    ) {

        return new ArrayList<>();
    }


    public void deleteUser(
            @Min(value = 1, message = "ID must be greater than 0")
            @Max(value = Integer.MAX_VALUE, message = "ID exceeds maximum limit")
            Long id) {

    }


    public UserDTO getUserById(
            @Min(1)
            @Max(Integer.MAX_VALUE)
            Long id
    ) {
        return new UserDTO();
      }



    public UserDTO updateUser(
            @Min(1)
            @Max(Integer.MAX_VALUE)
            Long id, @
            Valid UserDTO
            userDto
    ) {
        /*
        UserEntity existingEntity = userRs.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cannot update. Person with ID " + id + NOT_FOUND_SUFFIX));

        if (userDto.getCdsid() != null) existingEntity.setCdsid(userDto.getCdsid());
        if (userDto.getHashPassword() != null) existingEntity.setHashPassword(userDto.getHashPassword());

        // Note: active is a boolean primitive in your entity, check logic accordingly
        existingEntity.setActive(userDto.isActive());

        // add the 2 way factor properties
        existingEntity.setMfaEnabled(userDto.isMfaEnabled());
        existingEntity.setSecret(userDto.getSecret());
        existingEntity.setTempSecret(userDto.getTempSecret());

        // 2. Person Association Update
        if (userDto.getPersonId() != null) {
            personRs.findById(userDto.getPersonId())
                    .ifPresent(existingEntity::setPerson);
        }

        // 3. Roles Update Logic (The "Complete" part)
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            // Clear current roles to trigger orphan removal
            existingEntity.getRoles().clear();

            // Map DTOs to Entities and link them to the existing user
            userDto.getRoles().forEach(roleDto -> {
                UserRolesEntity newRole = UserRolesEntity.builder()
                        .role(roleDto.name())           // From RolesDTO.name
                        .user(existingEntity)           // Maintain bidirectional link
                        .build();
                existingEntity.getRoles().add(newRole);
            });
        } else {
            // If the DTO roles list is empty or null, remove all existing roles
            existingEntity.getRoles().clear();
        }



        // Save the updated entity (JPA will merge the changes)
        UserEntity savedEntity = userRs.saveAndFlush(existingEntity);

        return userMap.toDto(savedEntity);

         */

        return new UserDTO();
    }

    //region filter implementation
    public List<UserResponceDTO> filter(@Valid UserFilterDTO filter) {

        /*
        Specification<UserEntity> spec = (root, query, cb) -> {

            // 2. Combine with your existing filters
            List<Specification<UserEntity>> predicates = getSpecifications(filter);
            return Specification.allOf(predicates).toPredicate(root, query, cb);
        };

        // 2. Fetch the list of entities from the repository
        List<UserEntity> userEntities = userRs.findAll(spec);

        // 3. Map the list of entities to the list of DTOs
        // Using the 'toResponseDtoList' method we added to your mapper
        return userMap.toResponseDtoList(userEntities);

         */
        return new ArrayList<>();
    }


    //endregion
}

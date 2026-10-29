package com.qr.custom_qr_authentication.service.database;

import com.qr.custom_qr_authentication.entity.PersonsEntity;
import com.qr.custom_qr_authentication.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MemoryDatasource implements IDatasource {

    List<UserEntity> users = new ArrayList<>();

    @Override
    public Optional<UserEntity> getUserByUsername(String name) {
        if (name == null || users == null) {
            return null;
        }

        // Find user by username using Java Streams (case-sensitive or insensitive as needed)
        return users.stream()
                .filter(user -> name.equals(user.getCdsid()))
                .findFirst();
    }

    @PostConstruct
    private void init(){
        users.add(UserEntity.builder()
                        .id(1L)
                        .cdsid("mgerrit2")
                        .oid("a9bb28e5-3309-4a8b-b040-59cf3fe0f3a7")
                        .active(true)
                        .person(PersonsEntity.builder()
                                .firstname("Marc")
                                .lastname("Gerrits")
                                .email("gerrits.marc@hotmail.com")
                                .build())
                .build());

        // User 2: Active
        users.add(UserEntity.builder()
                .id(2L)
                .cdsid("jdoe1")
                .oid("b8cc39f6-4410-5b9c-c151-60df4gf1g4b8")
                .active(true)
                .person(PersonsEntity.builder()
                        .firstname("Jane")
                        .lastname("Doe")
                        .email("jane.doe@example.com")
                        .build())
                .build());

        // User 3: Inactive (active = false)
        users.add(UserEntity.builder()
                .id(3L)
                .cdsid("jsmith9")
                .oid("c9dd40a7-5521-6c0d-d262-71eg5hg2h5c9")
                .active(false)
                .person(PersonsEntity.builder()
                        .firstname("John")
                        .lastname("Smith")
                        .email("john.smith@example.com")
                        .build())
                .build());

    }

}

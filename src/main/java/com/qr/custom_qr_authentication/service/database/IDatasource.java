package com.qr.custom_qr_authentication.service.database;

import com.qr.custom_qr_authentication.entity.UserEntity;

import java.util.Optional;

public interface IDatasource {
    Optional<UserEntity> getUserByUsername(String name);
}

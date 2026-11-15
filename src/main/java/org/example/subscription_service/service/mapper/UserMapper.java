package org.example.subscription_service.service.mapper;

import org.example.subscription_service.model.UserEntity;
import org.example.subscription_service.model.dto.UserRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserRegisterDto toDto(UserEntity entity, Long maxFileSize) {
        return new UserRegisterDto(
                entity.getLogin(),
                entity.getSubType(),
                entity.getExpireAt(),
                maxFileSize
        );
    }
}

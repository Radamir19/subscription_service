package org.example.subscription_service.model.dto;

import java.time.OffsetDateTime;

public record UserRegisterDto(String login, String subType, OffsetDateTime expireAt) {
}

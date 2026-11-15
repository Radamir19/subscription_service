package org.example.subscription_service.service;

import lombok.RequiredArgsConstructor;
import org.example.subscription_service.messaging.event.SubscriptionExpiredEvent;
import org.example.subscription_service.model.OutboxEntity;
import org.example.subscription_service.model.UserEntity;
import org.example.subscription_service.model.dto.UserRegisterDto;
import org.example.subscription_service.model.dto.status.SubType;
import org.example.subscription_service.model.exceptions.UserNotFoundException;
import org.example.subscription_service.repository.OutboxRepository;
import org.example.subscription_service.repository.UserRepository;
import org.example.subscription_service.service.mapper.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher publisher;

    public UserRegisterDto getUser(String login) {
        UserEntity entity = userRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с таким логином нет."));
        return userMapper.toDto(entity, getMaxFileSize(entity));
    }

    @Transactional
    public void downgradeSubType() {
        List<UserEntity> expiredUsers = userRepository.findBySubTypeNotAndExpireAtBeforeOrderByExpireAtAsc(
                SubType.FREE.name(), OffsetDateTime.now(), Limit.of(10));
        List<OutboxEntity> outboxEntities = new ArrayList<>();
        for(UserEntity entity : expiredUsers) {
            entity.setSubType(SubType.FREE.name());
            entity.setExpireAt(null);
            SubscriptionExpiredEvent event = new SubscriptionExpiredEvent(
                    entity.getLogin(),
                    entity.getSubType(),
                    entity.getExpireAt(),
                    getMaxFileSize(entity)
            );
            OutboxEntity outbox = new OutboxEntity();
            outbox.setId(UUID.randomUUID());
            outbox.setAggregate(entity.getLogin());
            outbox.setPayload(objectMapper.writeValueAsString(event));
            outbox.setCreatedAt(OffsetDateTime.now());
            outboxEntities.add(outbox);
            publisher.publishEvent(event);
        }
        outboxRepository.saveAll(outboxEntities);
        userRepository.saveAll(expiredUsers);
    }

    private Long getMaxFileSize(UserEntity entity) {
        boolean activePaid = SubType.PAID.name().equals(entity.getSubType())
                && entity.getExpireAt() != null
                && entity.getExpireAt().isAfter(OffsetDateTime.now());
        Long maxFileSizeBytes = activePaid
                ? null
                : 100L * 1024 * 1024;
        return maxFileSizeBytes;
    }
}

package org.example.subscription_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.subscription_service.messaging.event.SubscriptionExpiredEvent;
import org.example.subscription_service.model.OutboxEntity;
import org.example.subscription_service.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, SubscriptionExpiredEvent> template;
    private final OutboxRepository outboxRepository;

    @Value("${app.kafka.topics.sub_expired}")
    private String topic;

    @Scheduled(fixedDelay = 1000)
    public void publishing() {
        List<OutboxEntity> batch = outboxRepository.findByPublishedAtIsNullOrderByCreatedAtAsc(Limit.of(10));
        for(OutboxEntity outbox : batch) {
            try{
                SubscriptionExpiredEvent event = objectMapper.readValue(outbox.getPayload(), SubscriptionExpiredEvent.class);
                template.send(topic, outbox.getAggregate(), event).get();
                outbox.setPublishedAt(OffsetDateTime.now());
                outboxRepository.save(outbox);
            } catch(Exception e) {
                log.warn("Не удалось отправить, повтор на следующем тике", e.getMessage());
            }
        }
    }
}

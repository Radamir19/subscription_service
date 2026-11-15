package org.example.subscription_service.service;

import lombok.RequiredArgsConstructor;
import org.example.subscription_service.messaging.event.SubscriptionExpiredEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SubscriptionCacheInvalidator {

    private final StringRedisTemplate redisTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSubscriptionChanged(SubscriptionExpiredEvent event) {
        redisTemplate.delete("subscriptions::" + event.login());
    }
}
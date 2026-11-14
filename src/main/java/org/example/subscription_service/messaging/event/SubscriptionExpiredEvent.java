package org.example.subscription_service.messaging.event;

import java.time.OffsetDateTime;

public record SubscriptionExpiredEvent(String login, String currentPlan, OffsetDateTime expiredAt) {
}

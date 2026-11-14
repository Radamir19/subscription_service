package org.example.subscription_service.service;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionScheduler {
    private final UserService userService;

    @Scheduled(cron = "${app.cleanup.cron}")
    @SchedulerLock(
            name = "downgradeSubType",
            lockAtMostFor = "10m",
            lockAtLeastFor = "1m"
    )
    public void checkExpiredSubscriptions() {
        userService.downgradeSubType();
    }
}

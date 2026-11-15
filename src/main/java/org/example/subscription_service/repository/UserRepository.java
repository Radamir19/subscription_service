package org.example.subscription_service.repository;

import org.example.subscription_service.model.OutboxEntity;
import org.example.subscription_service.model.UserEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findBySubTypeNotAndExpireAtBeforeOrderByExpireAtAsc(
            String subType,
            OffsetDateTime now,
            Limit limit
    );
}

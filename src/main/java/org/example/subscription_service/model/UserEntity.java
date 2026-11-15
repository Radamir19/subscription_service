package org.example.subscription_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "id")
    private String login;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "sub_type",
            referencedColumnName = "sub_type",
            nullable = false
    )
    private SubscriptionEntity subscription;

    private OffsetDateTime expireAt;
}

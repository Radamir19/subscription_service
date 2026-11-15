package org.example.subscription_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionEntity {

    @Id
    private String subType;

    private Long maxFileSize;

    @OneToMany(mappedBy = "subscription")
    private List<UserEntity> users = new ArrayList<>();
}

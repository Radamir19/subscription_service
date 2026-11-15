--liquibase formatted sql
--changeset subscription_service:3
CREATE TABLE shedlock(
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP    NOT NULL,
    locked_at  TIMESTAMP    NOT NULL,
    locked_by  VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);
--rollback DROP TABLE shedlock;
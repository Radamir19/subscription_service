--liquibase formatted sql
--changeset subscription_service:1
CREATE TABLE users(
    id VARCHAR PRIMARY KEY,
    sub_type VARCHAR(10) NOT NULL DEFAULT 'FREE',
    expire_at TIMESTAMPTZ
);
--rollback DROP TABLE user;
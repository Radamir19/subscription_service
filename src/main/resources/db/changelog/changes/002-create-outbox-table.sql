--liquibase formatted sql
--changeset subscription_service:2
CREATE TABLE outbox (
    id           UUID PRIMARY KEY,
    aggregate    VARCHAR(100) NOT NULL,
    payload      VARCHAR NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at TIMESTAMPTZ,
    attempts     SMALLINT NOT NULL DEFAULT 0
);
--rollback DROP TABLE outbox;
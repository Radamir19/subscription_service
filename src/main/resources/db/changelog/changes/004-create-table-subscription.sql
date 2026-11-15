--liquibase formatted sql
--changeset subscription-service:4
CREATE TABLE subscription(
    sub_type VARCHAR PRIMARY KEY,
    max_file_size BIGINT
);
--rollback DROP TABLE subscription;
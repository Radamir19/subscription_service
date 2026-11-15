--liquibase formatter sql
--changeset subscription-service:5
ALTER TABLE users
ADD CONSTRAINT fk_users_subscription
FOREIGN KEY (sub_type) REFERENCES subscription(sub_type);
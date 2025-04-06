ALTER TABLE local_accounts MODIFY active_username VARCHAR(20) AS(
    IF(`status` = -1, NULL, username)
) VIRTUAL;

ALTER TABLE social_accounts MODIFY active_username_code VARCHAR(20) AS(
    IF(`status` = -1, NULL, username_code)
) VIRTUAL;
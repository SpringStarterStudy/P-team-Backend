ALTER TABLE favorites DROP FOREIGN KEY FK_favorites_user;
ALTER TABLE favorites DROP FOREIGN KEY FK_favorites_profile;

ALTER TABLE favorites DROP PRIMARY KEY;

ALTER TABLE favorites ADD COLUMN favorite_id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;

ALTER TABLE favorites ADD CONSTRAINT uk_favorites_user_profile UNIQUE (user_id, profile_id);

ALTER TABLE favorites ADD CONSTRAINT FK_favorites_user FOREIGN KEY (user_id) REFERENCES accounts(id);
ALTER TABLE favorites ADD CONSTRAINT FK_favorites_profile FOREIGN KEY (profile_id) REFERENCES trainer_profile(profile_id);
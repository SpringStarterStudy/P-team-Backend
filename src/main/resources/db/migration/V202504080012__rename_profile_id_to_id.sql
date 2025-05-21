ALTER TABLE favorites DROP FOREIGN KEY FK_favorites_profile;

ALTER TABLE trainer_profile ADD COLUMN id BIGINT;
UPDATE trainer_profile SET id = profile_id;
ALTER TABLE trainer_profile DROP COLUMN profile_id;
ALTER TABLE trainer_profile MODIFY COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE favorites ADD CONSTRAINT FK_favorites_profile FOREIGN KEY (profile_id) REFERENCES trainer_profile(id);
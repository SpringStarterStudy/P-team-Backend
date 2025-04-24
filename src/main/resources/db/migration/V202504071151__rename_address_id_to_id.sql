ALTER TABLE trainer_profile DROP FOREIGN KEY FK_trainer_profile_address;

ALTER TABLe trainer_address ADD COLUMN id BIGINT;
UPDATE trainer_address SET id = address_id;
ALTER TABLE trainer_address DROP COLUMN address_id;
ALTER TABLE trainer_address MODIFY COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE trainer_profile ADD CONSTRAINT FK_trainer_profile_address FOREIGN KEY (address_id) REFERENCES trainer_address(id);
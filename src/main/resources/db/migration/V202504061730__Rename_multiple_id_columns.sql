ALTER TABLE `review_image`
CHANGE COLUMN `id` `review_image_id` BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE `review_reaction`
CHANGE COLUMN `reaction_id` `review_reaction_id` BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE `review_reply`
CHANGE COLUMN `id` `review_reply_id` BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE `review_tag_attribute`
CHANGE COLUMN `id` `review_tag_attribute_id` BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE `review_tag`
CHANGE COLUMN `id` `review_tag_id` BIGINT NOT NULL AUTO_INCREMENT;
-- 민형
CREATE TABLE `accounts` (
	`id`	BIGINT	NOT NULL	AUTO_INCREMENT,
	`name`	VARCHAR(20)	NOT NULL,
	`nickname`	VARCHAR(20)	NOT NULL,
	`role`	TINYINT	NOT NULL	DEFAULT 1	COMMENT '1(ROLE_USER)/2(ROLE_TRAINER)',
	`entity_type`	TINYINT	NOT NULL	COMMENT '1(로컬))/2(소셜)',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`deleted_at`	TIMESTAMP	NULL,
    PRIMARY KEY(`id`)
);

-- active_username: 가상 컬럼
	-- WHEN `status` = -1 THEN username ELSE NULL
CREATE TABLE `local_accounts`(
	`id`	BIGINT	NOT NULL,
	`username`	VARCHAR(20)	NOT NULL,
	`password`	VARCHAR(70)	NOT NULL,
	`email`	VARCHAR(255)	NOT NULL,
	`status`	TINYINT	NOT NULL	COMMENT '-1(삭제)/0(정지)/1(활성)/2(미인증)',
	-- `active_username`	VARCHAR(20)	NULL	UNIQUE
    PRIMARY KEY(`id`)
);

-- active_username_code: 가상 컬럼
	-- WHEN `status` = -1 THEN username_code ELSE NULL
CREATE TABLE `social_accounts` (
	`id`	BIGINT	NOT NULL,
	`username_code`	VARCHAR(50)	NOT NULL,
	`type`	TINYINT	NOT NULL	COMMENT '1(카카오)/2(네이버)',
	`status`	TINYINT	NOT NULL	COMMENT '-1(삭제)/0(정지)/1(활성)/2(미인증)',
	-- `active_username_code`	VARCHAR(50)	NULL	UNIQUE,
    PRIMARY KEY(`id`)
);

CREATE TABLE `schedule` (
	`id`	BIGINT	NOT NULL	AUTO_INCREMENT,
	`user_accounts_id`	BIGINT	NOT NULL,
	`trainer_accounts_id`	BIGINT	NOT NULL,
	`start_time`	TIMESTAMP	NOT NULL,
	`end_time`	TIMESTAMP	NOT NULL,
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`deleted_at`	TIMESTAMP	NULL,
    PRIMARY KEY(`id`)
);

-- FK

ALTER TABLE `local_accounts` ADD CONSTRAINT `FK_accounts_TO_local_accounts_1` FOREIGN KEY(`id`)
REFERENCES `accounts`(`id`);

ALTER TABLE `social_accounts` ADD CONSTRAINT `FK_accounts_TO_social_accounts_1` FOREIGN KEY(`id`)
REFERENCES `accounts`(`id`);

ALTER TABLE `schedule` ADD CONSTRAINT FK_schedule_TO_accounts_1 FOREIGN KEY(user_accounts_id)
REFERENCES accounts(id);

ALTER TABLE `schedule` ADD CONSTRAINT FK_schedule_TO_accounts_2 FOREIGN KEY(trainer_accounts_id)
REFERENCES accounts(id);

-- UK

ALTER TABLE accounts ADD CONSTRAINT UK_accounts_ON_nickname UNIQUE KEY(nickname);

ALTER TABLE local_accounts ADD CONSTRAINT UK_local_accounts_ON_email UNIQUE KEY(email);

-- etc
ALTER TABLE local_accounts ADD COLUMN active_username VARCHAR(20) AS(
	CASE
		WHEN `status` = -1
		THEN username
        ELSE NULL
	END
) VIRTUAL,
ADD UNIQUE KEY UK_local_accounts_ON_active_username(active_username);

ALTER TABLE social_accounts ADD COLUMN active_username_code VARCHAR(20) AS(
	CASE
		WHEN `status` = -1
		THEN username_code
        ELSE NULL	
	END
) VIRTUAL,
ADD UNIQUE KEY UK_social_accounts_ON_active_username_code(active_username_code);

-- 유림
CREATE TABLE `chat_room` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    `user_accounts_id` BIGINT NOT NULL COMMENT 'USER_ACCOUNT ID',
    `trainer_accounts_id` BIGINT NOT NULL COMMENT 'TRAINER_ACCOUNT ID',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
);

CREATE TABLE `workout_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    `trainer_accounts_id` BIGINT NOT NULL COMMENT 'TRAINER_ACCOUNT ID',
    `user_accounts_id` BIGINT NOT NULL COMMENT 'USER_ACCOUNT ID',
    `status` ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING (대기) ◦ APPROVED (승인) ◦ REJECTED (거절)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL,
    `training_date` DATE NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE `chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    `chat_room_id` BIGINT NOT NULL COMMENT 'CHAT_ROOM ID',
    `sender_id` BIGINT NOT NULL COMMENT 'SENDER ACCOUNT ID',
    `receiver_id` BIGINT NOT NULL COMMENT 'RECEIVER ACCOUNT ID',
    `message` TEXT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_read` BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY(`id`)
);

-- FK
ALTER TABLE `chat_room`
    ADD CONSTRAINT FK_chat_room_user_accounts FOREIGN KEY (`user_accounts_id`) REFERENCES `accounts`(`id`),
    ADD CONSTRAINT FK_chat_room_trainer_accounts FOREIGN KEY (`trainer_accounts_id`) REFERENCES `accounts`(`id`);

ALTER TABLE `workout_request`
    ADD CONSTRAINT FK_workout_request_user_accounts FOREIGN KEY (`user_accounts_id`) REFERENCES `accounts`(`id`),
    ADD CONSTRAINT FK_workout_request_trainer_accounts FOREIGN KEY (`trainer_accounts_id`) REFERENCES `accounts`(`id`);

ALTER TABLE `chat_message`
    ADD CONSTRAINT FK_chat_message_chat_room FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room`(`id`),
    ADD CONSTRAINT FK_chat_message_sender FOREIGN KEY (`sender_id`) REFERENCES `accounts`(`id`),
    ADD CONSTRAINT FK_chat_message_receiver FOREIGN KEY (`receiver_id`) REFERENCES `accounts`(`id`);

-- UK
ALTER TABLE `chat_room`
    ADD CONSTRAINT UK_chat_room_user_trainer UNIQUE (`user_accounts_id`, `trainer_accounts_id`);
    
-- 성민
-- 트레이너 프로필 테이블
CREATE TABLE trainer_profile (
    profile_id BIGINT AUTO_INCREMENT,
    user_id BIGINT NOT NULL,  
    address_id BIGINT NULL,
    profile_img TEXT NULL,
    intro TEXT NULL,
    credit INT NULL DEFAULT 0,
    contact_start_time TIME NULL,
    contact_end_time TIME NULL,
    is_name_public BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY(profile_id)
);

-- 트레이너 주소 테이블
CREATE TABLE trainer_address (
    address_id BIGINT AUTO_INCREMENT,
    number_address VARCHAR(255) NOT NULL,
    road_address VARCHAR(255) NOT NULL,
    detail_address VARCHAR(100),
    postal_code VARCHAR(20) NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    PRIMARY KEY(address_id)
);

-- 트레이너 즐겨찾기(찜) 테이블
CREATE TABLE favorites (
    user_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (user_id, profile_id)
);

-- FK
ALTER TABLE trainer_profile 
ADD CONSTRAINT FK_trainer_profile_user FOREIGN KEY (user_id) REFERENCES accounts(id);

ALTER TABLE trainer_profile 
ADD CONSTRAINT FK_trainer_profile_address FOREIGN KEY (address_id) REFERENCES trainer_address(address_id);

ALTER TABLE favorites 
ADD CONSTRAINT FK_favorites_user FOREIGN KEY (user_id) REFERENCES accounts(id);

ALTER TABLE favorites 
ADD CONSTRAINT FK_favorites_profile FOREIGN KEY (profile_id) REFERENCES trainer_profile(profile_id);

-- 다연
-- 리뷰 테이블
CREATE TABLE `review` (
  `review_id` BIGINT NOT NULL AUTO_INCREMENT,
  `trainer_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `schedule_id` BIGINT NOT NULL,
  `rating` DECIMAL(2,1) NOT NULL,
  `content` TEXT NOT NULL,
  `pt_purpose` ENUM('다이어트', '근력강화', '체형교정', '재활치료') NULL,
  `pt_session_count` INT NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP NOT NULL,
  PRIMARY KEY (`review_id`),
  CONSTRAINT `fk_review_trainer` FOREIGN KEY (`trainer_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
);

-- 리뷰 태그 속성 테이블
CREATE TABLE `review_tag_attribute` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `is_active` TINYINT NOT NULL,
  PRIMARY KEY (`id`)
);

-- 리뷰 태그 테이블
CREATE TABLE `review_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `review_id` BIGINT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_review_tag_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`),
  CONSTRAINT `fk_review_tag_attribute` FOREIGN KEY (`tag_id`) REFERENCES `review_tag_attribute` (`id`)
);

-- 리뷰 반응 테이블
CREATE TABLE `review_reaction` (
  `reaction_id` BIGINT NOT NULL AUTO_INCREMENT,
  `review_id` BIGINT NOT NULL,
  `account_id` BIGINT NOT NULL,
  `reaction` ENUM('thumbs_up', 'muscle', 'fire', 'smile') NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP NOT NULL,
  PRIMARY KEY (`reaction_id`),
  UNIQUE KEY `uk_review_account` (`review_id`, `account_id`),
  CONSTRAINT `fk_review_reaction_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`),
  CONSTRAINT `fk_review_reaction_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
);

-- 리뷰 이미지 테이블
CREATE TABLE `review_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `review_id` BIGINT NOT NULL,
  `image_url` VARCHAR(255) NOT NULL,
  `display_order` TINYINT NOT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
  `upload_date` TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP NOT NULL,
  `file_name` VARCHAR(255) NULL,
  `file_type` VARCHAR(30) NULL,
  `file_size` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_review_image_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`)
);

-- 리뷰 답글 테이블
CREATE TABLE `review_reply` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `review_id` BIGINT NOT NULL,
  `trainer_id` BIGINT NOT NULL,
  `content` TEXT NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_review_reply_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`),
  CONSTRAINT `fk_review_reply_trainer` FOREIGN KEY (`trainer_id`) REFERENCES `accounts` (`id`)
);

-- 윤서
-- ---------------------------------------------------------
-- 1. Payment 테이블 (결제 정보)
-- ---------------------------------------------------------

CREATE TABLE `payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key, AUTO_INCREMENT',
    `account_id` BIGINT NOT NULL COMMENT '계정 테이블(Account)의 id 참조',
    `product_id` VARCHAR(255) DEFAULT NULL COMMENT '제품 식별자',
    `payment_method` ENUM('SIMPLE_PAYMENT', 'CARD_PAYMENT') NOT NULL COMMENT '결제 수단',
    `payment_money` BIGINT NOT NULL COMMENT '결제된 금액 (원 단위)',
    `transaction_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제 일시',
    `status` ENUM('SUCCESS', 'CANCELLED', 'FAILED') NOT NULL COMMENT '결제 상태',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_payment_account` FOREIGN KEY (`account_id`) REFERENCES `accounts`(`id`) -- 계정 accounts
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- 2. ExternalPayment 테이블 (외부 결제 연동 정보)
-- ---------------------------------------------------------
CREATE TABLE `external_payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key, AUTO_INCREMENT',
    `payment_id` BIGINT NOT NULL COMMENT 'Payment 테이블의 id 참조',
    `gateway_type` ENUM('KAKAO', 'TOSS') NOT NULL,
    `external_payment_key` BIGINT NOT NULL COMMENT '상세 결제 정보 조회 키',
    `cid` BIGINT NOT NULL,
    `approved_at` DATETIME NOT NULL,
    `cancelled_at` DATETIME DEFAULT NULL,
    `amount` JSON NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_externalpayment_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment`(`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- 3. Credit 테이블 (계정별 크레딧 정보)
-- ---------------------------------------------------------
CREATE TABLE `credit` (
    `id` BIGINT NOT NULL COMMENT 'Primary key (고유 식별자, 상황에 따라 계정 id와 동일하게 할 수도 있음)',
    `account_id` BIGINT NOT NULL COMMENT '계정 테이블(Account)의 id 참조',
    `credit_balance` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_credit_account` (`account_id`),
    CONSTRAINT `fk_credit_account` FOREIGN KEY (`account_id`) REFERENCES `accounts`(`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- 4. CreditLog 테이블 (크레딧 변경 내역)
-- ---------------------------------------------------------
CREATE TABLE `credit_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key, AUTO_INCREMENT',
    `account_id` BIGINT NOT NULL COMMENT '계정 테이블(Account)의 id 참조',
    `credit_difference` INT NOT NULL COMMENT '크레딧 변동량',
    `credit_info` ENUM('PURCHASING', 'TRAINING') NOT NULL COMMENT '크레딧 변화 사유',
    `credit_balance` INT NOT NULL COMMENT '변경 후 크레딧 잔액',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_creditlog_account` FOREIGN KEY (`account_id`) REFERENCES `accounts`(`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE `external_payment`
MODIFY COLUMN `external_payment_key` VARCHAR(20) NOT NULL COMMENT '상세 결제 정보 조회 키',
MODIFY COLUMN `cid` VARCHAR(20) NOT NULL;

ALTER TABLE credit
MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

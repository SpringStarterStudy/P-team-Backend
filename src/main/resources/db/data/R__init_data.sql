-- 계정
-- role: 1(회원), 2(트레이너)
-- entity_type: 1(로컬), 2(소셜)  
insert ignore into accounts(id, `name`, nickname, role, entity_type) values
    (1, '회원1', '회원닉네임1', 1, 1),	-- 이메일 인증 o
    (2, '회원2', '회원닉네임2', 1, 1),	-- 이메일 인증 o
    (3, '트레이너1', '트레이너닉네임1', 2, 1),	-- 이메일 인증 o
    (4, '트레이너2', '트레이너닉네임2', 2, 1);	-- 이메일 인증 o

-- 로컬 계정
-- status: -1(삭제)/0(정지)/1(활성)/2(미인증)
-- 계정 정보
    -- 회원 pk: @user_id1
    -- 회원 테스트 계정 ID: usertest1
    -- 비밀번호: 1234567aA!
    
    -- 트레이너 pk: @trainer_id1
    -- 트레이너 테스트 계정 ID: trainertest1
    -- 비밀번호: 1234567aA!
    
-- 계정 세팅
set @user_id1 = (select id from accounts where nickname = '회원닉네임1');
set @trainer_id1 = (select id from accounts where nickname = '트레이너닉네임1');
set @user_id2 = (select id from accounts where nickname = '회원닉네임2');
set @trainer_id2 = (select id from accounts where nickname = '트레이너닉네임2');

set @test_pwd = '$2a$10$eXthWEeajRbGgRfvlfVBl.LlD6jDWoyAgyRSDa.FdRUTM4vfnYh86';	-- password = 1234567aA!
insert ignore into local_accounts(id, username, `password`, email, `status`) values
    (@user_id1,'usertest1', @test_pwd, 'usertest1@gmail.com', 1),	-- 이메일 인증 o
    (@trainer_id1, 'trainertest1', @test_pwd, 'trainertest1@gmail.com', 1),	-- 이메일 인증 o
    (@user_id2, 'usertest2', @test_pwd, 'usertest2@gmail.com', 1),	-- 이메일 인증 o
    (@trainer_id2, 'trainertest2', @test_pwd, 'trainertest2@gmail.com', 1);	-- 이메일 인증 o


-- 채팅방
INSERT IGNORE INTO chat_room (id, user_accounts_id, trainer_accounts_id, created_at) VALUES
(1, @user_id1, @trainer_id1, '2025-03-25 10:00:00'),
(2, @user_id2, @trainer_id2, '2025-03-25 11:30:00');

-- 채팅 메시지
INSERT IGNORE INTO chat_message (id, chat_room_id, sender_id, receiver_id, message, created_at, is_read) VALUES
(1, 1, @user_id1, @trainer_id1, '안녕하세요! 운동 상담 가능할까요?', '2025-03-25 10:05:00', FALSE),
(2, 1, @trainer_id1, @user_id1, '네! 어떤 운동을 희망하시나요?', '2025-03-25 10:07:00', TRUE),
(3, 2, @user_id2, @trainer_id2, '트레이너님, PT 예약 가능할까요?', '2025-03-25 11:32:00', FALSE),
(4, 2, @user_id2, @trainer_id2, '오늘 저녁 8시에 예약 가능할까요?', '2025-03-25 15:05:00', FALSE);

-- 운동 신청
INSERT IGNORE INTO workout_request (id, trainer_accounts_id, user_accounts_id, status, created_at, updated_at, deleted_at, training_date, start_time, end_time) VALUES
(1, @trainer_id1, @user_id1, 'PENDING', '2025-03-24 09:00:00', '2025-03-24 09:00:00', NULL, '2025-03-26', '10:00:00', '11:00:00'),
(2, @trainer_id2, @user_id2, 'APPROVED', '2025-03-24 11:00:00', '2025-03-25 08:00:00', NULL, '2025-03-27', '14:00:00', '15:30:00'),
(3, @trainer_id2, @user_id1, 'REJECTED', '2025-03-23 13:30:00', '2025-03-24 10:00:00', NULL, '2025-03-28', '09:00:00', '10:00:00');


-- 트레이너 주소
INSERT IGNORE INTO trainer_address (id, number_address, road_address, detail_address, postal_code, latitude, longitude)
VALUES
(201, '서울특별시 서초구 방배동 451-11', '서울 서초구 방배로4길 15', '301호', '06500', 37.4780267618979, 126.9835848745),
(202, '서울 강남구 역삼동 814-6', '서울 강남구 강남대로 438', '202호', '06123', 37.5016428104731, 127.026336096148),
(203, '부산광역시 해운대구 좌동 101-3', '부산 해운대구 해운대로 101', '101호', '48095', 35.1847966076786, 129.177240294733),
(204, '대구광역시 수성구 범어동 222-1', '대구 수성구 동원로1길 35', '201호', '42031', 35.8596023276928, 128.638240415548),
(205, '서울특별시 암사동 503-23', '서울 강동구 올림픽로 787', '-101호', '61478', 37.5514795712342, 127.127719522317);

-- 트레이너 프로필
INSERT IGNORE INTO trainer_profile (id, user_id, address_id, profile_img, intro, credit, contact_start_time, contact_end_time, is_name_public, created_at, updated_at, deleted_at)
VALUES
(1, @trainer_id1, 201, NULL, '자기 소개, 운동 방향, 이력 등', 50000, '09:00:00', '18:00:00', TRUE, '2025-03-01 12:00', NULL, NULL),
(2, @trainer_id2, 202, NULL, '체형 교정 전문가', 30000, '10:00:00', '19:00:00', FALSE, '2025-03-03 15:00', NULL, NULL);

-- 트레이너 즐겨찾기
INSERT IGNORE INTO favorites (user_id, profile_id, created_at, deleted_at)
VALUES
(@user_id1, @trainer_id1, '2025-03-20', NULL),
(@user_id2, @trainer_id2, '2025-03-29', NULL);


-- 일정
insert IGNORE into `schedule`(user_accounts_id, trainer_accounts_id, start_time, end_time) values
    (@user_id2, @trainer_id2, '2025-03-27 14:00:00', '2025-03-27 15:30:00');

-- 리뷰 태그 속성 review_tag_attribute
INSERT IGNORE INTO review_tag_attribute (review_tag_attribute_id, name, is_active) VALUES
(1, '친절해요', 1),
(2, '전문성이 있어요', 1),
(3, '시설이 좋아요', 1),
(4, '효과가 좋아요', 1),
(5, '소통이 원활해요', 1),
(6, '동작이 정확해요', 1),
(7, '가격이 합리적이에요', 1),
(8, '맞춤 운동을 제공해요', 1),
(9, '환경이 깨끗해요', 1),
(10, '시간을 잘 지켜요', 1);

-- 리뷰 review
INSERT IGNORE INTO review (review_id, trainer_id, user_id, schedule_id, rating, content, pt_purpose, pt_session_count, created_at, updated_at) VALUES
(1, @trainer_id1, @user_id1, 1, 4.5, '강트레이너님 덕분에 체중 감량에 성공했어요! 맞춤 운동이 정말 효과적이었습니다.', '다이어트', 10, '2023-11-10 09:30:00', '2023-11-10 09:30:00');

-- 리뷰 태그 review_tag
INSERT IGNORE INTO review_tag (review_tag_id, review_id, tag_id) VALUES
(1, 1, 1), -- 리뷰1: 친절해요
(2, 1, 4), -- 리뷰1: 효과가 좋아요
(3, 1, 8); -- 리뷰1: 맞춤 운동을 제공해요

-- 리뷰 이미지 review_image
INSERT IGNORE INTO review_image (review_image_id, review_id, image_url, display_order, is_active, upload_date, updated_at, file_name, file_type, file_size) VALUES
(1, 1, 'https://example.com/images/review1_1.jpg', 1, 1, '2023-11-10 09:30:00', '2023-11-10 09:30:00', 'before_after1.jpg', 'image/jpeg', 256000);

-- 리뷰 반응 review_reaction
INSERT IGNORE INTO review_reaction (review_reaction_id, account_id, reaction, created_at, updated_at) VALUES
(1, @user_id2, 'thumbs_up', '2023-11-11 08:20:00', '2023-11-11 08:20:00'),
(1, @trainer_id1, 'fire', '2023-11-11 12:45:00', '2023-11-11 12:45:00'),
(1, @trainer_id2, 'muscle', '2023-11-11 15:35:00', '2023-11-11 15:35:00');

-- 리뷰 답글 review_reply
INSERT IGNORE INTO review_reply (review_reply_id, review_id, trainer_id, content, created_at, updated_at) VALUES
(1, 1, @trainer_id1, '김회원님, 좋은 리뷰 감사합니다! 앞으로도 더 좋은 결과를 위해 노력하겠습니다.', '2023-11-10 15:20:00', '2023-11-10 15:20:00');

-- 1. Payment 테이블 (결제 정보)
INSERT IGNORE INTO payment (account_id, product_id, payment_method, payment_money, status)
VALUES (@user_id1, 'PROD0', 'SIMPLE_PAYMENT', 15000, 'SUCCESS');

SET @payment_id := LAST_INSERT_ID();

-- 2. ExternalPayment 테이블 (외부 결제 연동 정보)
INSERT IGNORE INTO external_payment (payment_id, gateway_type, external_payment_key, cid, approved_at, cancelled_at, amount)
VALUES (@payment_id, 'KAKAO', 'T1234567890123456789', '18FCCB1CFCFC3E5ED89F', '2025-03-30 10:00:00', NULL, '{"total": 15000}');

-- 3. 크레딧 구매 내역 (구매 시 trainer_id는 필요 없음)
INSERT IGNORE INTO credit_log (account_id, credit_difference, credit_info, credit_balance)
VALUES (@user_id1, 150, 'PURCHASING', 150);

-- 3. 크레딧 소모 내역 (예: 운동신청, trainer_id 지정)
INSERT IGNORE INTO credit_log (account_id, credit_difference, credit_info, credit_balance)
VALUES (@user_id1, -50, 'TRAINING', 100);

-- 4. Credit 테이블 (계정별 크레딧 정보)
INSERT IGNORE INTO credit (account_id, credit_balance)
VALUES (@user_id1, 100);
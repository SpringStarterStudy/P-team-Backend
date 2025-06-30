-- 계정
-- role: 1(회원), 2(트레이너)
-- entity_type: 1(로컬), 2(소셜)
insert ignore into accounts(id, `name`, nickname, role, entity_type) values
    (1, '회원1', '회원닉네임1', 1, 1),
    (2, '회원2', '회원닉네임2', 1, 1),
    (3, '트레이너1', '트레이너닉네임1', 1, 1),
    (4, '트레이너2', '트레이너닉네임2', 1, 1);

-- 로컬 계정
-- status: -1(삭제)/0(정지)/1(활성)/2(미인증)
-- 계정 정보
-- 회원 pk: @user_id1
-- 회원 테스트 계정 ID: usertest1
-- 비밀번호: 1234567aA!

-- 계정 세팅
set @user_id1 = (select id from accounts where nickname = '회원닉네임1');
set @user_id2 = (select id from accounts where nickname = '회원닉네임2');

set @test_pwd = '{bcrypt}$2a$10$eXthWEeajRbGgRfvlfVBl.LlD6jDWoyAgyRSDa.FdRUTM4vfnYh86';	-- password = 1234567aA!
insert ignore into local_accounts(id, username, `password`, email, `status`) values
    (@user_id1,'usertest1', @test_pwd, 'usertest1@gmail.com', 1),	-- 활성
    (@user_id2, 'usertest2', @test_pwd, 'usertest2@gmail.com', 0),	-- 정지
    (3,'trainertest1', @test_pwd, 'trainertest1@gmail.com', 1),	-- 활성
    (4,'trainertest2', @test_pwd, 'trainertest2@gmail.com', 1);	-- 활성


-- 일정
insert IGNORE into `schedule`(user_accounts_id, trainer_accounts_id, start_time, end_time) values
    (1, 5, '2025-05-12 14:00:00', '2025-05-12 15:30:00'),
    (1, 6, '2025-05-17 12:00:00', '2025-05-12 13:00:00'),
    (1, 5, '2025-05-24 17:00:00', '2025-05-12 18:00:00');
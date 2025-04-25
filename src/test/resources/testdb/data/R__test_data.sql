-- 계정
-- role: 1(회원), 2(트레이너)
-- entity_type: 1(로컬), 2(소셜)
insert ignore into accounts(id, `name`, nickname, role, entity_type) values
    (1, '회원1', '회원닉네임1', 1, 1),
    (2, '회원2', '회원닉네임2', 1, 1);

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
    (@user_id2, 'usertest2', @test_pwd, 'usertest2@gmail.com', 0);	-- 정지

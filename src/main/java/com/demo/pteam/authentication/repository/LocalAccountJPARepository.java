package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalAccountJPARepository extends JpaRepository<LocalAccountEntity, Long> {
    //  TODO: 나중에 필요 컬럼만 조회하도록 수정
    Optional<LocalAccountEntity> findByActiveUsername(String username);
}

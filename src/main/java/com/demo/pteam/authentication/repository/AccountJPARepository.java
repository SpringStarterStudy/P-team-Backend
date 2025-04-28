package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJPARepository extends JpaRepository<AccountEntity, Long>, AccountJPARepositoryCustom {
}

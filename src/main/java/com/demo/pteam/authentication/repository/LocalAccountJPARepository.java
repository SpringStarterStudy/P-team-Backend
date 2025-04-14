package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalAccountJPARepository extends JpaRepository<LocalAccountEntity, Long>, LocalAccountJPARepositoryCustom {
}

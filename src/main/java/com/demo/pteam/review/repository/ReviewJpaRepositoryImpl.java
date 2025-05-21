package com.demo.pteam.review.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ReviewJpaRepositoryImpl implements ReviewJpaRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

}

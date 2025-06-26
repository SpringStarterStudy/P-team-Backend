package com.demo.pteam.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {
    protected SoftDeletableEntity(LocalDateTime createdAt) {
        super(createdAt);
    }

    @Column(insertable = false)
    private LocalDateTime deletedAt;
}

package com.demo.pteam.global.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {

    protected LocalDateTime deletedAt;
}

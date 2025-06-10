package com.demo.pteam.authentication.repository.entity;

import com.demo.pteam.authentication.repository.converter.RoleConverter;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.global.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.INTEGER)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AccountEntity extends SoftDeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;

    @Convert(converter = RoleConverter.class)
    @Column(updatable = false)
    private Role role;

    protected AccountEntity(LocalDateTime createdAt, String name, String nickname, Role role) {
        super(createdAt);
        this.name = name;
        this.nickname = nickname;
        this.role = role;
    }
}

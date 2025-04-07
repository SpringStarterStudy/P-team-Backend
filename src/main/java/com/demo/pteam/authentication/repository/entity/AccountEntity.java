package com.demo.pteam.authentication.repository.entity;

import com.demo.pteam.authentication.repository.converter.RoleConverter;
import com.demo.pteam.authentication.domain.Role;
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
public abstract class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;

    @Convert(converter = RoleConverter.class)
    @Column(updatable = false)
    private Role role;

    // TODO: 임시 구현
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    protected AccountEntity(String name, String nickname, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.role = role;
    }
}

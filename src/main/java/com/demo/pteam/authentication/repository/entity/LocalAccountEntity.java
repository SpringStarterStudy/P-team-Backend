package com.demo.pteam.authentication.repository.entity;

import com.demo.pteam.authentication.repository.converter.AccountStatusConverter;
import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "local_accounts")
@DiscriminatorValue(value = "1")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocalAccountEntity extends AccountEntity {
    @Column(updatable = false)
    private String username;
    private String password;
    private String email;

    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status;

    @Column(insertable = false, updatable = false)
    private String activeUsername;

    @Builder
    public LocalAccountEntity(String name, String nickname, Role role, String username, String password, String email, AccountStatus status) {
        super(name, nickname, role);
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }
}

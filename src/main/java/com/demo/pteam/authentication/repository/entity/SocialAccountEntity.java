package com.demo.pteam.authentication.repository.entity;

import com.demo.pteam.authentication.repository.converter.AccountStatusConverter;
import com.demo.pteam.authentication.repository.converter.SocialTypeConverter;
import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.domain.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_accounts")
@DiscriminatorValue(value = "2")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SocialAccountEntity extends AccountEntity {
    private String usernameCode;

    @Convert(converter = SocialTypeConverter.class)
    private SocialType type;

    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status;

    @Column(insertable = false, updatable = false)
    private String activeUsernameCode;

    @Builder
    public SocialAccountEntity(String name, String nickname, Role role, String usernameCode, SocialType type, AccountStatus status) {
        super(name, nickname, role);
        this.usernameCode = usernameCode;
        this.type = type;
        this.status = status;
    }
}

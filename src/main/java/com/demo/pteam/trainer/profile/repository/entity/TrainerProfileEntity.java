package com.demo.pteam.trainer.profile.repository.entity;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.entity.SoftDeletableEntity;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "trainer_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerProfileEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AccountEntity trainer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private TrainerAddressEntity address;

    @Lob
    private String profileImg;

    @Lob
    private String intro;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer credit;

    private LocalTime contactStartTime;
    private LocalTime contactEndTime;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isNamePublic;

    @Builder
    public TrainerProfileEntity(AccountEntity trainer, TrainerAddressEntity address, String profileImg,
                                String intro, Integer credit, LocalTime contactStartTime,
                                LocalTime contactEndTime, Boolean isNamePublic) {
        this.trainer = trainer;
        this.address = address;
        this.profileImg = profileImg;
        this.intro = intro;
        this.credit = credit != null ? credit : 0;
        this.contactStartTime = contactStartTime;
        this.contactEndTime = contactEndTime;
        this.isNamePublic = isNamePublic != null ? isNamePublic : true;
    }

}

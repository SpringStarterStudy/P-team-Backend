package com.demo.pteam.trainer.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "trainer_profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    /* TODO: 회원 관계 설정

     */

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

    // TODO: BaseEntity 상속
}

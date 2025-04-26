package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TrainerProfileRepositoryImpl implements TrainerProfileRepository {

    private final TrainerProfileJPARepository trainerProfileJPARepository;
    private final EntityManager em;

    @Override
    public void save(TrainerProfile profile) {

        AccountEntity trainer = em.getReference(AccountEntity.class, profile.getUserId());
        TrainerAddressEntity address = em.getReference(TrainerAddressEntity.class, profile.getAddressId());

        TrainerProfileEntity entity = TrainerProfileEntity.builder()
                .trainer(trainer)
                .address(address)
                .profileImg(profile.getProfileImg())
                .intro(profile.getIntro())
                .credit(profile.getCredit())
                .contactStartTime(profile.getContactStartTime())
                .contactEndTime(profile.getContactEndTime())
                .isNamePublic(profile.getIsNamePublic())
                .build();

        trainerProfileJPARepository.save(entity);
    }
}
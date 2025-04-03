package com.demo.pteam.review.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review_tag_attribute")
@Getter @Setter
@NoArgsConstructor
public class ReviewTagAttributeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_tag_attribute_id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}

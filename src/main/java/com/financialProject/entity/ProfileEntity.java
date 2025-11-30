package com.financialProject.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "activation_token")
    private String activationToken;

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = false;
        }
    }
}

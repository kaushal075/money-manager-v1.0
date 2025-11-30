package com.financialProject.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private Long id;

    private String fullName;

    private String email;

    private String password;
    private  String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String activationToken;
}

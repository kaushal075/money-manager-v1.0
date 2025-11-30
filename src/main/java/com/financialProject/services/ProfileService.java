package com.financialProject.services;

import com.financialProject.dto.AuthDto;
import com.financialProject.dto.ProfileDto;
import com.financialProject.entity.ProfileEntity;
import com.financialProject.repository.ProfileRepository;
import com.financialProject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Value("${app.activation.url}")
    private String activationUrl;

    public ProfileDto registerProfile(ProfileDto profileDto){
        ProfileEntity newProfile = toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile =profileRepository.save(newProfile);

        String activationLink = activationUrl+"/api/v1.0/activate?token="+newProfile.getActivationToken();
        String subject = "Activate your money manager account..";
        String body = "Go Through this link to activate it :"+activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject, body);

        return toDto(newProfile);

    }
    public ProfileEntity toEntity(ProfileDto profileDto){
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .password(passwordEncoder.encode(profileDto.getPassword()))
                .profileImage(profileDto.getProfileImage())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }
    public ProfileDto toDto(ProfileEntity profileEntity){
        return ProfileDto.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImage(profileEntity.getProfileImage())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }
    public Boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken)
                .map(profile ->
                        {profile.setIsActive(true);
                profileRepository.save(profile);
                return true;}
                ).orElse(false);
    }
    public Boolean isAccountActive(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }
    public ProfileEntity getCurrentProfile(){
        SecurityContext authentication = SecurityContextHolder.getContext();
        return profileRepository.findByEmail(authentication.getAuthentication().getName())
                .orElseThrow(()->new UsernameNotFoundException("Profile Not Found with This email."));
    }
    public ProfileDto getPublicProfile(String email){
        ProfileEntity currentUser = null;
        if(email == null) {
            currentUser = getCurrentProfile();
        }
        else{
          currentUser =  profileRepository.findByEmail(email)
                    .orElseThrow(()->new UsernameNotFoundException("Profile Not Found with This email."));
        }
        return toDto(currentUser);
    }

    public Map<String, Object> authenticateTokenGeneration(AuthDto authDto) {
        try{

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
            //generating token...JWT
            String token = jwtUtil.generateToken(authDto.getEmail());
            return Map.of(
                    "token",token,
                    "user",getPublicProfile(authDto.getEmail()));

        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password.");
        }
    }
}

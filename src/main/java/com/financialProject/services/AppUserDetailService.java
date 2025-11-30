package com.financialProject.services;

import com.financialProject.entity.ProfileEntity;
import com.financialProject.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity existingProfile = profileRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("Profile not found from this "+email+" ."));

        return User.builder()
                .username(existingProfile.getEmail())
                .password(existingProfile.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}

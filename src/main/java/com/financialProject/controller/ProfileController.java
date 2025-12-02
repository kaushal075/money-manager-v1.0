package com.financialProject.controller;

import com.financialProject.dto.AuthDto;
import com.financialProject.dto.ProfileDto;
import com.financialProject.services.EmailService;
import com.financialProject.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;




    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto) {

        ProfileDto registeredProfile = profileService.registerProfile(profileDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);

    }
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token){
        boolean isActivated = profileService.activateProfile(token);
        if(isActivated){
            return ResponseEntity.status(HttpStatus.OK).body("Your account is successfully activated..");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Your account is not activated try again..");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto) {

        try {
            if (!profileService.isAccountActive(authDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message", "Account is not active.Please activate it first."
                ));
            }
                Map<String, Object> response = profileService.authenticateTokenGeneration(authDto);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage()
            ));

        }

    }



}

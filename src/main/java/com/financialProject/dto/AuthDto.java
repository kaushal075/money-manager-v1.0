package com.financialProject.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthDto {
    private String email;
    private String password;
    private String token;
}

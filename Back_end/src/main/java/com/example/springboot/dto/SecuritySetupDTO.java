package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecuritySetupDTO {
    private String username;
    private String securityQuestion;
    private String securityAnswer;

}
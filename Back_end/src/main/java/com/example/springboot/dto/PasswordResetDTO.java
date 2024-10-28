package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordResetDTO {
    private String username;

    private String securityAnswer;
    private String newPassword;
    private String confirmPassword;


}

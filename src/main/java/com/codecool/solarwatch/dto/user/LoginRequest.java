package com.codecool.solarwatch.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

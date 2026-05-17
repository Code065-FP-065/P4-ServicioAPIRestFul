package com.code065.alquilervehiculos.dto;

public class LoginResponseDTO {

    private String token;
    private String tokenType;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token) {
        this.token = token;
        this.tokenType = "Bearer";
    }

    public LoginResponseDTO(String token, String tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}

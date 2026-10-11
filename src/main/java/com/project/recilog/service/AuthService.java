package com.project.recilog.service;

import com.project.recilog.dto.auth.*;

public interface AuthService {
    void signUp(SignUpRequestDto dto);
    TokenResponseDto login(LoginRequestDto dto);
    TokenResponseDto refreshAccessToken(RefreshTokenRequestDto dto);
    void logout(Long userId);
}
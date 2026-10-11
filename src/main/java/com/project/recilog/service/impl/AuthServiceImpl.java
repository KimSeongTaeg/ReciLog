package com.project.recilog.service.impl;

import com.project.recilog.domain.RefreshToken;
import com.project.recilog.domain.User;
import com.project.recilog.dto.auth.*;
import com.project.recilog.repository.RefreshTokenRepository;
import com.project.recilog.repository.UserRepository;
import com.project.recilog.security.JwtUtil;
import com.project.recilog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일 주소입니다.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .provider("LOCAL")
                .role("ROLE_USER")
                .status("ACTIVE")
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getEmail(), user.getRole());
        String refreshTokenStr = jwtUtil.createRefreshToken(user.getUserId());

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId())
                .orElse(RefreshToken.builder().userId(user.getUserId()).build());

        refreshToken.updateToken(refreshTokenStr, LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenValidity() / 1000)
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public TokenResponseDto refreshAccessToken(RefreshTokenRequestDto dto) {
        String reqRefreshToken = dto.getRefreshToken();
        if (!jwtUtil.validateToken(reqRefreshToken)) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 Refresh Token입니다.");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(reqRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Refresh Token입니다."));

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtUtil.createAccessToken(user.getUserId(), user.getEmail(), user.getRole());

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(reqRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenValidity() / 1000)
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
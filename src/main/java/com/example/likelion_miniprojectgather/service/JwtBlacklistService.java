package com.example.likelion_miniprojectgather.service;

import com.example.likelion_miniprojectgather.domain.BlacklistToken;
import com.example.likelion_miniprojectgather.jwt.token.JwtTokenizer;
import com.example.likelion_miniprojectgather.repository.BlacklistTokenRepository;
import io.jsonwebtoken.Claims;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final BlacklistTokenRepository blacklistTokenRepository;
    private final JwtTokenizer jwtTokenizer;

    // 블랙리스트에 토큰 추가
    public void blacklistToken(String token) {
        LocalDateTime expirationTime = getExpirationTimeFromToken(token);
        blacklistTokenRepository.save(new BlacklistToken(token, expirationTime));
    }

    // 블랙리스트에 포함된 토큰인지 확인
    public boolean isBlacklisted(String token) {
        return blacklistTokenRepository.findByValue(token).isPresent();
    }

    // JWT 토큰에서 만료 시간 추출
    private LocalDateTime getExpirationTimeFromToken(String token) {
        Claims claims = jwtTokenizer.parseAccessToken(token);
        Instant instant = claims.getExpiration().toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        blacklistTokenRepository.deleteByExpired(now);
        System.out.println("만료된 블랙리스트 토큰 정리 완료 ");
    }
}
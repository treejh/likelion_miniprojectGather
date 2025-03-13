package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.RefreshToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByValue(String token);

    // 만료된 토큰 삭제 (DB 관리 목적)
    void deleteByExpired(LocalDateTime now);
}

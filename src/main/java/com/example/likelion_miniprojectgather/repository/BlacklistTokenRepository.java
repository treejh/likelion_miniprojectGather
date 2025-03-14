package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.BlacklistToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Integer> {

    Optional<BlacklistToken> findByValue(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistToken b WHERE b.expired < :expiredTime")
    void deleteByExpiredBefore(@Param("expiredTime") LocalDateTime expiredTime);
}

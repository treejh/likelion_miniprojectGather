package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
}

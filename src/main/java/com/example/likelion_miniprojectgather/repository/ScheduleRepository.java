package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

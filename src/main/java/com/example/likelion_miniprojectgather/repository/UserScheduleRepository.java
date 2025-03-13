package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule,Long> {
}

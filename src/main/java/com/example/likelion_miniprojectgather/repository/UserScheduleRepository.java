package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.Schedule;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserSchedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule,Long> {

}

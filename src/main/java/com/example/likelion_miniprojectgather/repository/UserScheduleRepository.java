package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.Schedule;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserSchedule;
import com.example.likelion_miniprojectgather.enumData.StatusEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule,Long> {
    //scheduleId를 통해서 UserSchedule에 속한 일정들을 가지고 온다.
    @Query("SELECT um FROM UserSchedule um WHERE um.schedule.id = :scheduleId and um.user.id=:userId")
    Optional<UserSchedule> findByScheduleIdAndUserId(@Param("scheduleId") Long scheduleId,@Param("userId") Long userId);


    //scheduleId를 통해서 UserSchedule에 속한 일정들을 가지고 온다.
    @Query("SELECT um.user FROM UserSchedule um WHERE um.schedule.id = :scheduleId and um.status=:status")
    Optional<List<User>> findByScheduleIdAndStatus(@Param("scheduleId") Long scheduleId,@Param("status") StatusEnum status);
}

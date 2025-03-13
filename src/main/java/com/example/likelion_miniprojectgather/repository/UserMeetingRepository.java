package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.Meeting;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserMeeting;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMeetingRepository extends JpaRepository<UserMeeting,Long> {

    //meetingId를 통해서 meeting에 속한 유저들을 가지고 온다.
    @Query("SELECT um.user FROM UserMeeting um WHERE um.meeting.id = :meetingId")
    Optional<List<User>> findUserByMeetingId(@Param("meetingId") Long meetingId);
}


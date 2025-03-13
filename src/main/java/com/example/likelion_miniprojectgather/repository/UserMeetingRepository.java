package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.UserMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMeetingRepository extends JpaRepository<UserMeeting,Long> {
}

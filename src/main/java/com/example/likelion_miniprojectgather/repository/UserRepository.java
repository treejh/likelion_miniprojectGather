package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

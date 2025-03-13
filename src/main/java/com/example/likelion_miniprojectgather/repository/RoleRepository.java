package com.example.likelion_miniprojectgather.repository;

import com.example.likelion_miniprojectgather.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}

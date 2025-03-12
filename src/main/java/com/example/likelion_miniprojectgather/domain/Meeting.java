package com.example.likelion_miniprojectgather.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="meetings")
@Getter
@Setter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "max_participants",nullable = false)
    private Long maxParticipants;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_meeting",
            joinColumns  = @JoinColumn(name="meeting_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    List<User> userList = new ArrayList<>();

}

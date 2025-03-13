package com.example.likelion_miniprojectgather.domain;


import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="meetings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "meeting",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<UserMeeting> userList = new ArrayList<>();

    @OneToMany(mappedBy = "meeting",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<Schedule> scheduleList = new ArrayList<>();
}

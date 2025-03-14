package com.example.likelion_miniprojectgather.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRequestDto {
    private String name;
    private String description;
    private Long maxParticipants;

}

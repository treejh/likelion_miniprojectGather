package com.example.likelion_miniprojectgather.dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MeetingListResponseDto{

        private Long id;
        private String name;
        private String description;
        private Long maxParticipants;
        private Long currentParticipants;

}


package com.example.likelion_miniprojectgather.dto.request;

import com.example.likelion_miniprojectgather.enumData.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class ScheduleRequestDto {
    String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 요청 받을 때 형식 지정
    LocalDate date;

    @DateTimeFormat(pattern = "HH:mm") // 요청 받을 때 형식 지정
    LocalTime time;
    String location;

}

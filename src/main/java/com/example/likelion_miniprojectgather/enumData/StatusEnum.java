package com.example.likelion_miniprojectgather.enumData;


import lombok.Getter;

@Getter
public enum StatusEnum {
    ATTENDING("참여"),
    MAYBE("대기"),
    NOT_ATTENDING("취소");

    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }
}



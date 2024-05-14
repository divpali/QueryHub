package com.divpali.QueryHub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {

    private Long id;
    private String content;
    private Timestamp answerCreatedTime;
    private UserDto user;
    private Set<AnswerResponseDto> nestedAnswers; // Represent nested answers

}

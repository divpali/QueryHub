package com.divpali.QueryHub.dto;

import com.divpali.QueryHub.entities.Tag;
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
public class AnswerDto {

    private Long id;
    private String content;
    private UserDto user;
}

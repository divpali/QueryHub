package com.divpali.QueryHub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    private String content;

    private Timestamp postCreatedTime;

    private Long userId;

    private Set<String> tagNames;
}

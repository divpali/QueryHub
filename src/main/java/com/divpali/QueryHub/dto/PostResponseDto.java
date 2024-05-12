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
public class PostResponseDto {

    private Long postId;
    private String postContent;
    private Timestamp postCreatedTime;
    private Long postUserId;
    private String postUsername;
    private Set<String> tagNames;
    private Set<AnswerDto> answers;
}

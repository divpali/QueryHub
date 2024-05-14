package com.divpali.QueryHub.dto;

import com.divpali.QueryHub.entities.Vote;
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

    /*private Long postId;
    private String postContent;
    private Timestamp postCreatedTime;
    private Long postUserId;
    private String postUsername;
    private Set<String> tagNames;
    private Set<AnswerResponseDto> answers;
    private Set<Vote> votes;*/
}

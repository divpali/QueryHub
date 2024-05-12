package com.divpali.QueryHub.services;

import com.divpali.QueryHub.dto.PostRequestDto;
import com.divpali.QueryHub.dto.PostResponseDto;
import com.divpali.QueryHub.dto.UserRequestDto;
import com.divpali.QueryHub.dto.UserResponseDto;
import com.divpali.QueryHub.entities.Post;
import com.divpali.QueryHub.entities.User;

public interface PostService {

    public PostResponseDto createPost(User user, PostRequestDto postRequestDto);

    public Post getPostById(Long postId);

    public Post save(Post post);
}

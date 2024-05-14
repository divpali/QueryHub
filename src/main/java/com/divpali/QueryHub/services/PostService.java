package com.divpali.QueryHub.services;

import com.divpali.QueryHub.dto.PostRequestDto;
import com.divpali.QueryHub.dto.PostResponseDto;
import com.divpali.QueryHub.entities.Post;
import com.divpali.QueryHub.entities.User;

import java.util.List;
import java.util.Optional;

public interface PostService {

    public PostResponseDto createPost(User user, PostRequestDto postRequestDto);

    public Optional<Post> getPostById(Long postId);

    public Post save(Post post);

    public List<Post> searchPostsByTagNames(List<String> tagNames);

    void votePost(Long postId, Long userId, int voteType);
}

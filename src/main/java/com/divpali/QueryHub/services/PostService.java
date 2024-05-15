package com.divpali.QueryHub.services;

import com.divpali.QueryHub.dto.PostRequestDto;
import com.divpali.QueryHub.dto.PostResponseDto;
import com.divpali.QueryHub.entities.Post;
import com.divpali.QueryHub.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostService {

    public PostResponseDto createPost(User user, PostRequestDto postRequestDto);

    public Optional<Post> getPostById(Long postId);

    public List<Post> getPostByUserId(Long userId);

    public Post save(Post post);

    public List<Post> searchPostsByTagNames(List<String> tagNames);

    public Set<Post> searchPostsByTagName(String tagName);

    public void votePost(Long postId, Long userId, int voteType);

    public List<Post> getAllPosts();
}

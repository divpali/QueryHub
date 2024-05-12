package com.divpali.QueryHub.services.impl;

import com.divpali.QueryHub.dto.PostRequestDto;
import com.divpali.QueryHub.dto.PostResponseDto;
import com.divpali.QueryHub.entities.Post;
import com.divpali.QueryHub.entities.Tag;
import com.divpali.QueryHub.entities.User;
import com.divpali.QueryHub.repository.PostRepository;
import com.divpali.QueryHub.repository.TagRepository;
import com.divpali.QueryHub.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Override
    public PostResponseDto createPost(User user, PostRequestDto postRequestDto) {

        ModelMapper modelMapper = new ModelMapper();

        Post post = modelMapper.map(postRequestDto, Post.class);
        post.setPostCreatedTime(new Timestamp(System.currentTimeMillis()));


        /*
        On stack overflow, you need 1500 reputation to create a new tag. Unfortunately, you don't have that much yet.
        If you did have 1500 rep, then you could create a new tag simply by adding the tag to a question. The tag will
        be created automatically.

        TO DO : add specific conditions to set new tags in the below logic
         */

        //set tags in post request
        Set<Tag> tagsOnPostRequest = new HashSet<>();
        Set<String> tagNames = new HashSet<>();

        Set<String> tagsInRequest = postRequestDto.getTagNames();
        if(tagsInRequest != null) {
            for (String tagName : tagsInRequest) {
                Tag tag = tagRepository.findByName(tagName);
                if(tag == null) {
                    tag = new Tag();    // create new tag - haven't put any specific conditions yet to set new tags
                    tag.setName(tagName);
                    tag = tagRepository.save(tag);
                    tagNames.add(tagName);
                }
                tagsOnPostRequest.add(tag);
            }
        }

        post.setTags(tagsOnPostRequest);

        PostResponseDto postResponseDto = modelMapper.map(postRepository.save(post), PostResponseDto.class);
        postResponseDto.setTagNames(tagNames);
        postResponseDto.setPostUserId(user.getId());
        postResponseDto.setPostUsername(user.getUsername());

        return postResponseDto;
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }
}

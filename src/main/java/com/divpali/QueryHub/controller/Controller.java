package com.divpali.QueryHub.controller;

import com.divpali.QueryHub.dto.*;
import com.divpali.QueryHub.entities.Answer;
import com.divpali.QueryHub.entities.Post;
import com.divpali.QueryHub.entities.Tag;
import com.divpali.QueryHub.entities.User;
import com.divpali.QueryHub.exception.UserRegistrationException;
import com.divpali.QueryHub.services.AnswerService;
import com.divpali.QueryHub.services.PostService;
import com.divpali.QueryHub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/query-hub")
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private AnswerService answerService;


    @PostMapping("/api/users/register")
    public ResponseEntity<?> createUserProfile(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto userResponse = userService.createUserProfile(userRequestDto);
            return ResponseEntity.ok(userResponse);
        } catch (UserRegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }

    @PostMapping("/api/posts/{userId}")
    public ResponseEntity<?> createPost(@PathVariable Long userId, @RequestBody PostRequestDto postRequestDto) {
        ModelMapper modelMapper = new ModelMapper();

        User user = userService.getUserById(userId);

        PostResponseDto postResponseDto = postService.createPost(user, postRequestDto);

        return ResponseEntity.ok(postResponseDto);
    }

    //users can only comment after they registered
    @PostMapping("/api/answers/{postId}/{userId}")
    public ResponseEntity<?> addCommentsToPost(@PathVariable Long postId, @PathVariable Long userId,
                                               @RequestBody AnswerRequestDto answerRequestDto) {

        User user = userService.getUserById(userId);

        Post post = postService.getPostById(postId);

        //create answer
        Answer answer = new Answer();
        answer.setContent(answerRequestDto.getAnswerContent());
        answer.setCommentCreatedTime(new Timestamp(System.currentTimeMillis()));
        answer.setUser(user);
        answer.setPost(post);
        answerService.save(answer);

        //set answer in post
        post.getAnswers().add(answer);
        postService.save(post);

        //set answer in user
        user.getAnswers().add(answer);
        userService.save(user);

        ModelMapper modelMapper = new ModelMapper();

        Set<Answer> answers = post.getAnswers();

        Set<AnswerDto> answerToPost = new HashSet<>();

        for (Answer ans : answers) {
            AnswerDto answerDto = new AnswerDto();
            answerDto.setId(ans.getId());
            answerDto.setContent(ans.getContent());
            UserDto userDto = new UserDto();
            userDto.setUserId(ans.getUser().getId());
            userDto.setUserName(ans.getUser().getUsername());
            answerDto.setUser(userDto);

            answerToPost.add(answerDto);
        }

        Set<Tag> tags = post.getTags();
        Set<String> tagNames = new HashSet<>();

        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }

        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);

        postResponseDto.setPostUserId(userId);
        postResponseDto.setPostUsername(user.getUsername());
        postResponseDto.setTagNames(tagNames);
        postResponseDto.setAnswers(answerToPost);

        return ResponseEntity.ok(postResponseDto);
    }

}

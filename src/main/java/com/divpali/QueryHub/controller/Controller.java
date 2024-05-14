package com.divpali.QueryHub.controller;

import com.divpali.QueryHub.VoteType;
import com.divpali.QueryHub.dto.*;
import com.divpali.QueryHub.entities.*;
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
import java.util.*;

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

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {

        Optional<User> optionalUser = userService.getUserById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setId(userId);
            userResponseDto.setUsername(user.getUsername());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setBio(user.getBio());

            return ResponseEntity.ok(userResponseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + userId);
        }

    }


    @PostMapping("/api/posts/{userId}")
    public ResponseEntity<?> createPost(@PathVariable Long userId, @RequestBody PostRequestDto postRequestDto) {

        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            PostResponseDto postResponseDto = postService.createPost(optionalUser.get(), postRequestDto);
            return ResponseEntity.ok(postResponseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + userId);
        }

    }

    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<?> getPostByPostId(@PathVariable Long postId) {
        Optional<Post> optionalPost = postService.getPostById(postId);

        if (optionalPost.isPresent()) {

            Post post = optionalPost.get();

            PostDto postDto = new PostDto();
            postDto.setPostId(post.getId());
            postDto.setPostContent(post.getContent());
            postDto.setPostCreatedTime(post.getPostCreatedTime());
            postDto.setPostUserId(post.getUser().getId());
            postDto.setPostUsername(post.getUser().getUsername());

            Set<String> tagNames = new HashSet<>();
            for (Tag tag : post.getTags()) {
                tagNames.add(tag.getName());
            }

            postDto.setTagNames(tagNames);

            Set<Answer> answers = post.getAnswers();
            Set<AnswerResponseDto> answerResponseDtoSet = new HashSet<>();

            for (Answer ans : answers) {
                AnswerResponseDto answerResponseDto = mapToAnswerResponseDto(ans);
                answerResponseDtoSet.add(answerResponseDto);
            }

            postDto.setAnswers(answerResponseDtoSet);

            return ResponseEntity.ok(postDto);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post not found with ID: " + postId);
        }


    }


    //users can only comment after they registered
    @PostMapping("/api/answers/{postId}/{userId}")
    public ResponseEntity<?> addAnswersToPost(@PathVariable Long postId, @PathVariable Long userId,
                                               @RequestBody AnswerRequestDto answerRequestDto) {

        Optional<User> optionalUser = userService.getUserById(userId);

        User user;
        if(optionalUser.isPresent()){
            user = optionalUser.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID "+ userId+" not found.");

        }

        Optional<Post> optionalPost = postService.getPostById(postId);

        Post post;
        if(optionalPost.isPresent()) {
            post = optionalPost.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post with ID " + postId + " not found.");
        }

        //create answer
        Answer answer = new Answer();
        answer.setContent(answerRequestDto.getAnswerContent());
        answer.setAnswerCreatedTime(new Timestamp(System.currentTimeMillis()));
        answer.setUser(user);
        answer.setPost(post);
        answerService.save(answer);

        //set answer in post
        post.getAnswers().add(answer);
        postService.save(post);

        //set answer in user
        user.getAnswers().add(answer);
        userService.save(user);

        Set<Tag> tags = post.getTags();
        Set<String> tagNames = new HashSet<>();

        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }

        ModelMapper modelMapper = new ModelMapper();

        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);

        postResponseDto.setPostUserId(userId);
        postResponseDto.setPostUsername(user.getUsername());
        postResponseDto.setTagNames(tagNames);
        postResponseDto.setVotes(post.getVotes());

        Set<Answer> answers = post.getAnswers();

        Set<AnswerResponseDto> answerDto = new HashSet<>();

        for (Answer ans : answers) {
            answerDto.add(mapToAnswerResponseDto(ans));
        }

        postResponseDto.setAnswers(answerDto);

        return ResponseEntity.ok(postResponseDto);
    }


    @PostMapping("/api/nestedAnswers/{parentId}/{userId}")
    public ResponseEntity<?> addNestedAnswer(@PathVariable Long parentId, @PathVariable Long userId,
                                             @RequestBody AnswerRequestDto answerRequestDto) {

        Optional<User> optionalUser = userService.getUserById(userId);

        User user;

        if(optionalUser.isPresent()){
            user = optionalUser.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID "+ userId+" not found.");

        }

        Answer parentAnswer = answerService.getAnswerById(parentId);
        if (parentAnswer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Parent answer with ID " + parentId + " not found.");
        }

        // Create nested answer
        Answer nestedAnswer = new Answer();
        nestedAnswer.setContent(answerRequestDto.getAnswerContent());
        nestedAnswer.setAnswerCreatedTime(new Timestamp(System.currentTimeMillis()));
        nestedAnswer.setUser(user);
        nestedAnswer.setParentAnswer(parentAnswer);
        answerService.save(nestedAnswer);

        // Add nested answer to parent answer
        parentAnswer.getNestedAnswers().add(nestedAnswer);
        answerService.save(parentAnswer);

        // Add nested answer to user
        user.getAnswers().add(nestedAnswer);
        userService.save(user);

        AnswerResponseDto nestedAnswerDto = mapToAnswerResponseDto(parentAnswer);
        return ResponseEntity.ok(nestedAnswerDto);
    }


    // Method to map Answer entity to AnswerResponseDto
    private AnswerResponseDto mapToAnswerResponseDto(Answer answer) {
        AnswerResponseDto answerDto = new AnswerResponseDto();
        answerDto.setId(answer.getId());
        answerDto.setContent(answer.getContent());
        answerDto.setAnswerCreatedTime(new Timestamp(System.currentTimeMillis()));

        UserDto userDto = new UserDto();
        userDto.setUserId(answer.getUser().getId());
        userDto.setUserName(answer.getUser().getUsername());
        answerDto.setUser(userDto);

        // Map nested answers recursively
        Set<AnswerResponseDto> nestedAnswersDto = new HashSet<>();
        for (Answer nestedAnswer : answer.getNestedAnswers()) {
            nestedAnswersDto.add(mapToAnswerResponseDto(nestedAnswer));
        }
        answerDto.setNestedAnswers(nestedAnswersDto);

        return answerDto;
    }

    @GetMapping("/api/search/tags")
    public ResponseEntity<?> searchPostsByTags(@RequestBody List<String> tagNames) {
        List<Post> posts = postService.searchPostsByTagNames(tagNames);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/api/post/vote/{userId}/{postId}/{voteType}")
    public ResponseEntity<?> addVoteToPost(@PathVariable Long userId, @PathVariable Long postId, @PathVariable int voteType) {
        try {
            postService.votePost(postId, userId, voteType);
            return ResponseEntity.ok("Vote recorded successfully");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    
    @GetMapping("/api/post/upVote/{postId}")
    public ResponseEntity<?> getUpvoteCountForPost(@PathVariable Long postId) {

        Optional<Post> postOptional = postService.getPostById(postId);
        long upvoteCount = 0;
        if(postOptional.isPresent()) {
            Post post = postOptional.get();
            Set<Vote> votes = post.getVotes();

            upvoteCount = votes.stream()
                    .filter(a -> a.getVoteType() == VoteType.UPVOTE)
                    .count();
        }
        
        return ResponseEntity.ok(upvoteCount);
    }

    @GetMapping("/api/post/downVote/{postId}")
    public ResponseEntity<?> getDownvoteCountForPost(@PathVariable Long postId) {

        Optional<Post> postOptional = postService.getPostById(postId);
        long downVoteCount = 0;
        if(postOptional.isPresent()) {
            Post post = postOptional.get();
            Set<Vote> votes = post.getVotes();

            downVoteCount = votes.stream()
                    .filter(a -> a.getVoteType() == VoteType.DOWNVOTE)
                    .count();
        }

        return ResponseEntity.ok(downVoteCount);
    }

}






























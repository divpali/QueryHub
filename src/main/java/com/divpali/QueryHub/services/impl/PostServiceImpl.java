package com.divpali.QueryHub.services.impl;

import com.divpali.QueryHub.VoteType;
import com.divpali.QueryHub.dto.PostRequestDto;
import com.divpali.QueryHub.dto.PostResponseDto;
import com.divpali.QueryHub.entities.Post;
import com.divpali.QueryHub.entities.Tag;
import com.divpali.QueryHub.entities.User;
import com.divpali.QueryHub.entities.Vote;
import com.divpali.QueryHub.repository.PostRepository;
import com.divpali.QueryHub.repository.TagRepository;
import com.divpali.QueryHub.repository.UserRepository;
import com.divpali.QueryHub.repository.VoteRepository;
import com.divpali.QueryHub.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @Override
    public PostResponseDto createPost(User user, PostRequestDto postRequestDto) {

        //create Post
        Post post = new Post();
        post.setContent(postRequestDto.getContent());
        post.setPostCreatedTime(new Timestamp(System.currentTimeMillis()));
        post.setUser(user);


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
                }
                tagNames.add(tagName);
                tagsOnPostRequest.add(tag);
            }
        }

        post.setTags(tagsOnPostRequest);
        Post savedPost = postRepository.save(post);

        ModelMapper modelMapper = new ModelMapper();
        PostResponseDto postResponseDto = modelMapper.map(savedPost, PostResponseDto.class);
        postResponseDto.setTagNames(tagNames);
        postResponseDto.setPostUserId(user.getId());
        postResponseDto.setPostUsername(user.getUsername());
        postResponseDto.setPostContent(postRequestDto.getContent());

        return postResponseDto;
    }

    @Override
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    public List<Post> getPostByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> searchPostsByTagNames(List<String> tagNames) {

        List<Post> total_posts = new ArrayList<>();
        for (String tagName : tagNames) {
            List<Post> posts = postRepository.findByTagsName(tagName);
            total_posts.addAll(posts);
        }
        return total_posts;
    }

    @Override
    public Set<Post> searchPostsByTagName(String tagName) {
        return postRepository.findByTagsName(tagName).stream().collect(Collectors.toSet());
    }

    @Transactional  //making entire operation of voting a post atomic
    @Override
    public void votePost(Long postId, Long userId, int voteType) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with ID: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        if (post.getVotes().stream().anyMatch(vote -> vote.getUser().equals(user))) {
            throw new IllegalArgumentException("User has already upvoted this post");
        }


        //create the vote --> voteType = 0 - upVote, voteType = -1 - downVote

        Vote vote = new Vote();
        vote.setPost(post);
        vote.setUser(user);
        if(voteType == 0) {
            vote.setVoteType(VoteType.UPVOTE);
        } else {
            vote.setVoteType(VoteType.DOWNVOTE);
        }

        voteRepository.save(vote);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

}





























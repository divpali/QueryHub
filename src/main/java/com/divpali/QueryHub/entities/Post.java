package com.divpali.QueryHub.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Timestamp postCreatedTime;

    //ManyToOne relationship with User
    @ManyToOne  //many posts can belong to one user
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("user")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    //OneToMany relationship with Answer - each post can have multiple answers
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("post")
    private Set<Answer> answers = new HashSet<>();

    //OneToMany relationship with Answer - each post can have multiple votes
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("post")
    private Set<Vote> votes = new HashSet<>();

    // store upVote and downVote count to denormalize the filed
//    private int upvoteCount;
//    private int downvoteCount;

}

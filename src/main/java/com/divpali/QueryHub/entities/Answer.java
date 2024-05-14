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
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Timestamp answerCreatedTime;

    // ManyToOne relationship with Post - multiple answers can belong to the same post.
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // ManyToOne relationship with User - multiple answers can be authored by the same user.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // OneToMany relationship with itself for nested answers
    @OneToMany(mappedBy = "parentAnswer", cascade = CascadeType.ALL)
    private Set<Answer> nestedAnswers = new HashSet<>();

    // ManyToOne relationship with itself for nested answers
    @ManyToOne
    @JoinColumn(name = "parent_answer_id")
    private Answer parentAnswer;

}

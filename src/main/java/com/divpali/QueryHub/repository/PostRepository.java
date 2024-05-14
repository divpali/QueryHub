package com.divpali.QueryHub.repository;

import com.divpali.QueryHub.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByTagsName(String tagName);
}

package com.divpali.QueryHub.repository;

import com.divpali.QueryHub.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    public Tag findByName(String name);
}

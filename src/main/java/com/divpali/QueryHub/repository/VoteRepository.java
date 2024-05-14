package com.divpali.QueryHub.repository;

import com.divpali.QueryHub.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}

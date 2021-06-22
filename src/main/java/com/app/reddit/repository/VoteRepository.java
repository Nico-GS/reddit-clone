package com.app.reddit.repository;

import com.app.reddit.models.Post;
import com.app.reddit.models.User;
import com.app.reddit.models.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}

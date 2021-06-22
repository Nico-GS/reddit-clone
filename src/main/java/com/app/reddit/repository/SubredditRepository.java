package com.app.reddit.repository;

import com.app.reddit.models.SubReddit;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubredditRepository extends CrudRepository<SubReddit, Long> {

    Optional<SubReddit> findByName(String subredditName);

}

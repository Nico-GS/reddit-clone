package com.app.reddit.repository;

import com.app.reddit.models.Post;
import com.app.reddit.models.SubReddit;
import com.app.reddit.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findAllBySubReddit(SubReddit subReddit);
    List<Post> findByUser(User user);

}

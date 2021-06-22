package com.app.reddit.repository;

import com.app.reddit.models.Comment;
import com.app.reddit.models.Post;
import com.app.reddit.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);

}

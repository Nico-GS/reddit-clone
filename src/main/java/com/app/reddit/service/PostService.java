package com.app.reddit.service;

import com.app.reddit.dto.PostRequest;
import com.app.reddit.dto.PostResponse;
import com.app.reddit.exception.PostNotFoundException;
import com.app.reddit.exception.SubredditNotFoundException;
import com.app.reddit.exception.UserNotFoundException;
import com.app.reddit.models.*;
import com.app.reddit.repository.*;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final VoteRepository voteRepository;

    private boolean checkVoteType(Post post, VoteType voteType) {
        if(authService.isLoggedIn()) {
            Optional<Vote> voteForPostForUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostForUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubReddit().getName())
                .voteCount(post.getVoteCount())
                .commentCount(commentRepository.findByPost(post).size())
                .duration(TimeAgo.using(post.getCreationDate().toEpochMilli()))
                .upVote(checkVoteType(post, VoteType.UPVOTE))
                .downVote(checkVoteType(post, VoteType.DOWNVOTE))
                .build();
    }

    private Post mapToPost(PostRequest postRequest) {
        SubReddit subReddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        Post newPost = Post.builder()
                .postTitle(postRequest.getPostTitle())
                .url(postRequest.getUrl())
                .description(postRequest.getDescription())
                .voteCount(0)
                .user(authService.getCurrentUser())
                .creationDate(Instant.now())
                .subReddit(subReddit)
                .build();
        subReddit.getPosts().add(newPost);
        return newPost;
    }

    public PostResponse save(PostRequest postRequest) {
        return mapToResponse(postRepository.save(mapToPost(postRequest)));
    }

    public List<PostResponse> getAllPost() {
        return StreamSupport
                .stream(postRepository.findAll().spliterator(), false)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PostResponse findByID (Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID : " + id));
        return mapToResponse(post);
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
        SubReddit subReddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id: " + id));
        return subReddit.getPosts().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username : " + username));
        return postRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

}

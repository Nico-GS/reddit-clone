package com.app.reddit.controller;

import com.app.reddit.dto.PostRequest;
import com.app.reddit.dto.PostResponse;
import com.app.reddit.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> addPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPost() {
        return new ResponseEntity<>(postService.getAllPost(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostByID(@PathVariable Long id) {
        return new ResponseEntity<>(postService.findByID(id), HttpStatus.OK);
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<List<PostResponse>> getPostBySubreddit(@PathVariable Long id) {
        return new ResponseEntity<>(postService.getPostsBySubreddit(id), HttpStatus.OK);
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return new ResponseEntity<>(postService.getPostsByUsername(name), HttpStatus.OK);
    }

}

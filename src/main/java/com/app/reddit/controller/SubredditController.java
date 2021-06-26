package com.app.reddit.controller;

import com.app.reddit.dto.SubredditDTO;
import com.app.reddit.service.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {

    SubredditService subredditService;

    @GetMapping
    public List<SubredditDTO> getAllSubreddits() {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    public SubredditDTO getSubreddit(@PathVariable Long id) {
        return subredditService.getSubreddit(id);
    }

    @PostMapping
    public SubredditDTO addSubreddit(@RequestBody @Valid SubredditDTO subredditDTO) {
        return subredditService.save(subredditDTO);
    }

}

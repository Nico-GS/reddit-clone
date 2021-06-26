package com.app.reddit.service;

import com.app.reddit.dto.SubredditDTO;
import com.app.reddit.exception.SubredditNotFoundException;
import com.app.reddit.models.SubReddit;
import com.app.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    private SubredditDTO mapToDTO (SubReddit subReddit) {
        return SubredditDTO.builder()
                .id(subReddit.getId())
                .name(subReddit.getName())
                .description(subReddit.getDescription())
                .postCount(subReddit.getPosts().size())
                .build();
    }

    private SubReddit mapToSubreddit (SubredditDTO subredditDTO) {
        return SubReddit.builder().name("/r/" + subredditDTO.getName())
                .description(subredditDTO.getDescription())
                .user(authService.getCurrentUser())
                .creationDate(Instant.now())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDTO> getAll() {
        return StreamSupport
                .stream(subredditRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubredditDTO save (SubredditDTO subredditDTO) {
        SubReddit subReddit = subredditRepository.save(mapToSubreddit(subredditDTO));
        subredditDTO.setId(subReddit.getId());
        return subredditDTO;
    }

    @Transactional(readOnly = true)
    public SubredditDTO getSubreddit(Long id) {
        SubReddit subReddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with ID : " + id));
        return mapToDTO(subReddit);
    }

}

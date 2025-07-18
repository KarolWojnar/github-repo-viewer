package com.task.atipera.controller;

import com.task.atipera.dto.RepositoryResponse;
import com.task.atipera.service.GitHubRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GitHubRepoController {

    private final GitHubRepoService gitHubRepoService;

    @GetMapping("/users/{username}/repositories")
    public ResponseEntity<List<RepositoryResponse>> getRepositoryDetailsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(gitHubRepoService.getRepositoryDetailsByUsername(username));
    }
}

package com.task.atipera.service;

import com.task.atipera.dto.GitHubBranch;
import com.task.atipera.dto.GitHubRepository;
import com.task.atipera.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubClient {

    @Value("${github.api-base-url:https://api.github.com}")
    private String GITHUB_API_BASE_URL;
    private final static String GITHUB_USERS = "/users/";
    private final static String GITHUB_REPOS = "/repos";
    private final static String GITHUB_BRANCHES = "/branches";
    private final RestTemplate restTemplate;

    public List<GitHubRepository> getUserRepos(String username) {
        String url = GITHUB_API_BASE_URL + GITHUB_USERS + username + GITHUB_REPOS;
        log.info("Request to GitHub API: {}", url);

        try {
            ResponseEntity<List<GitHubRepository>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<GitHubRepository>>() {}
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(username);
            }
            throw e;
        }
    }

    public List<GitHubBranch> getRepositoryBranches(String owner, String repositoryName) {
        String url = GITHUB_API_BASE_URL + GITHUB_REPOS + "/" + owner + "/" + repositoryName + GITHUB_BRANCHES;

        ResponseEntity<List<GitHubBranch>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GitHubBranch>>() {}
        );

        return response.getBody();
    }
}

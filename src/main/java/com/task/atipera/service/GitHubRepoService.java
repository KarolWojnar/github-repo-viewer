package com.task.atipera.service;

import com.task.atipera.dto.BranchInfo;
import com.task.atipera.dto.GitHubBranch;
import com.task.atipera.dto.GitHubRepository;
import com.task.atipera.dto.RepositoryResponse;
import com.task.atipera.exception.EmptyProfileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubRepoService {

    private final GitHubClient gitHubClient;

    public List<RepositoryResponse> getRepositoryDetailsByUsername(String username) {
        List<GitHubRepository> gitHubRepositories = gitHubClient.getUserRepos(username);
        if (gitHubRepositories.isEmpty()) {
            log.info("Profile {} has no public repositories", username);
            throw new EmptyProfileException(username);
        }
        log.debug("Found {} repos", gitHubRepositories.size());

        return gitHubRepositories.stream()
                .filter(repo -> !repo.isFork())
                .map(this::mapToRepositoryResponse)
                .collect(Collectors.toList());
    }

    private RepositoryResponse mapToRepositoryResponse(GitHubRepository repo) {
        List<GitHubBranch> branches = gitHubClient.getRepositoryBranches(
                repo.getOwner().getLogin(),
                repo.getName()
        );

        List<BranchInfo> branchInfos = branches.stream()
                .map(this::mapToBranchInfo)
                .toList();

        return new RepositoryResponse(
                repo.getName(),
                repo.getOwner().getLogin(),
                branchInfos
        );
    }

    private BranchInfo mapToBranchInfo(GitHubBranch branch) {
        return new BranchInfo(
                branch.getName(),
                branch.getCommit().getSha()
        );
    }
}

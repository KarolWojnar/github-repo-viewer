package com.task.atipera.dto;

import lombok.Data;

@Data
public class GitHubBranch {
    private String name;
    private GitHubCommit commit;

    @Data
    public static class GitHubCommit {
        private String sha;
    }
}
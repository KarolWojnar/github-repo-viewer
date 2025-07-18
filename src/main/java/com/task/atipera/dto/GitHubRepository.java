package com.task.atipera.dto;

import lombok.Data;

@Data
public class GitHubRepository {
    private String name;
    private GitHubOwner owner;
    private boolean fork;

    @Data
    public static class GitHubOwner {
        private String login;
    }
}

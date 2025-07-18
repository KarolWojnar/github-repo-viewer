package com.task.atipera.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchInfo {
    private String name;
    private String lastCommitSha;
}

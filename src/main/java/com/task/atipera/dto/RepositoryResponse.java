package com.task.atipera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepositoryResponse {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchInfo> branches;
}

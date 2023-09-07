package com.atipera.repoviewer.repository;

import com.atipera.repoviewer.model.BranchInfo;
import com.atipera.repoviewer.model.RepoResponse;

import java.util.List;

public interface IRepoRepository {
    List<RepoResponse> getRepositoriesByUsername(String username);
    List<BranchInfo> getBranchesInfo(String username, String repositoryName);
}

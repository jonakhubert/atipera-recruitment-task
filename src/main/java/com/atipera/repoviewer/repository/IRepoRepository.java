package com.atipera.repoviewer.repository;

import com.atipera.repoviewer.model.BranchResponse;
import com.atipera.repoviewer.model.RepoResponse;

import java.util.List;

public interface IRepoRepository {
    List<RepoResponse> getUserRepositories(String username);
    List<BranchResponse> getBranches(String username, String repositoryName);
}

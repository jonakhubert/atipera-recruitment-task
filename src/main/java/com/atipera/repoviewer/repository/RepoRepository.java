package com.atipera.repoviewer.repository;

import com.atipera.repoviewer.model.*;
import com.atipera.repoviewer.model.api.Branch;
import com.atipera.repoviewer.model.api.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RepoRepository implements IRepoRepository {

    private final RestTemplate restTemplate;

    @Autowired
    public RepoRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<RepoResponse> getUserRepositories(String username) {
        String apiUrl = "https://api.github.com/users/{username}/repos".replace("{username}", username);
        Repo[] repositories = restTemplate.getForObject(apiUrl, Repo[].class);

        return Optional.ofNullable(repositories)
            .stream()
            .flatMap(Arrays::stream)
            .filter(repository -> !repository.fork())
            .map(repository -> new RepoResponse(
                repository.name(),
                repository.owner().login(),
                getBranches(username, repository.name())
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<BranchResponse> getBranches(String username, String repoName) {
        String apiUrl = "https://api.github.com/repos/{username}/{repo}/branches"
            .replace("{username}", username)
            .replace("{repo}", repoName);

        Branch[] branches = restTemplate.getForObject(apiUrl, Branch[].class);

        return Optional.ofNullable(branches)
            .stream()
            .flatMap(Arrays::stream)
            .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
            .collect(Collectors.toList());
    }
}

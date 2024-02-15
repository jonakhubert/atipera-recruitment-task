package com.atipera.repoviewer.repository;

import com.atipera.repoviewer.model.*;
import com.atipera.repoviewer.model.api.Branch;
import com.atipera.repoviewer.model.api.Repo;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RepoRepository implements IRepoRepository {

    private final RestTemplate restTemplate;

    public RepoRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<RepoResponse> getUserRepositories(String username) {
        var apiUrl = "https://api.github.com/users/{username}/repos".replace("{username}", username);
        var repositories = restTemplate.getForObject(apiUrl, Repo[].class);

        return Arrays.stream(repositories != null ? repositories : new Repo[0])
                .filter(repository -> !repository.fork())
                .map(repository -> new RepoResponse(
                    repository.name(),
                    repository.owner().login(),
                    getBranches(username, repository.name())
                )).collect(Collectors.toList());
    }

    @Override
    public List<BranchResponse> getBranches(String username, String repoName) {
        var apiUrl = "https://api.github.com/repos/{username}/{repo}/branches"
            .replace("{username}", username)
            .replace("{repo}", repoName);

        var branches = restTemplate.getForObject(apiUrl, Branch[].class);

        return Arrays.stream(branches != null ? branches : new Branch[0])
                .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
                .collect(Collectors.toList());
    }
}

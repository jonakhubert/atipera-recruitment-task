package com.atipera.repoviewer.repository;

import com.atipera.repoviewer.model.*;
import com.atipera.repoviewer.model.api.Branch;
import com.atipera.repoviewer.model.api.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepoRepository implements IRepoRepository {

    private final RestTemplate restTemplate;

    @Autowired
    public RepoRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<RepoResponse> getRepositoriesByUsername(String username) {
        String apiUrl = "https://api.github.com/users/{username}/repos".replace("{username}", username);
        Repo[] repositories = restTemplate.getForObject(apiUrl, Repo[].class);
        List<RepoResponse> repoResponses = new ArrayList<>();

        if(repositories != null) {
            for(Repo repository : repositories) {
                if(!repository.fork()) {
                    repoResponses.add(new RepoResponse(
                        repository.name(),
                        repository.owner().login(),
                        getBranchesInfo(username, repository.name())
                    ));
                }
            }
        }

        return repoResponses;
    }

    @Override
    public List<BranchInfo> getBranchesInfo(String username, String repoName) {
        String apiUrl = "https://api.github.com/repos/{username}/{repo}/branches"
            .replace("{username}", username)
            .replace("{repo}", repoName);

        Branch[] branches = restTemplate.getForObject(apiUrl, Branch[].class);
        List<BranchInfo> branchInfos = new ArrayList<>();

        if (branches != null) {
            for (Branch branch : branches) {
                branchInfos.add(new BranchInfo(branch.name(), branch.commit().sha()));
            }
        }

        return branchInfos;
    }
}

package com.atipera.repoviewer.service;

import com.atipera.repoviewer.model.RepoResponse;
import com.atipera.repoviewer.repository.IRepoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {

    private final IRepoRepository repoRepository;

    public RepoService(IRepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public List<RepoResponse> getUserRepositories(String username) {
        return repoRepository.getUserRepositories(username);
    }
}

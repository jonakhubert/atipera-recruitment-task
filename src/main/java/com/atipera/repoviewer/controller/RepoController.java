package com.atipera.repoviewer.controller;

import com.atipera.repoviewer.model.RepoResponse;
import com.atipera.repoviewer.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/repository-management")
public class RepoController {

    private final RepoService repoService;

    @Autowired
    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping(path = "{username}")
    public ResponseEntity<List<RepoResponse>> getUserRepositories(
        @PathVariable String username,
        @RequestHeader(value = "Accept", defaultValue = "application/json") String acceptHeader
    ) {
        return ResponseEntity.ok(repoService.getUserRepositories(username));
    }
}

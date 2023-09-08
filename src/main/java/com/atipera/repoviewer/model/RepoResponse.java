package com.atipera.repoviewer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RepoResponse(
        @JsonProperty("name") String repositoryName,
        @JsonProperty("owner_login") String login,
        @JsonProperty("branches") List<BranchResponse> branches
) {}
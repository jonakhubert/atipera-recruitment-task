package com.atipera.repoviewer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchResponse(
        @JsonProperty("name") String name,
        @JsonProperty("last_commit_sha") String lastCommitSha
) {}

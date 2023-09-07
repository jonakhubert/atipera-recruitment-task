package com.atipera.repoviewer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchInfo(
        @JsonProperty("name") String name,
        @JsonProperty("last_commit_sha") String lastCommitSha
) {}

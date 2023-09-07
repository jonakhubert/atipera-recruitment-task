package com.atipera.repoviewer.model.api;

public record Repo(
        String name,
        Owner owner,
        boolean fork
) {}

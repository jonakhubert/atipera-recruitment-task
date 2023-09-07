package com.atipera.repoviewer.model.api;

public record Branch(
    String name,
    Commit commit
) {}

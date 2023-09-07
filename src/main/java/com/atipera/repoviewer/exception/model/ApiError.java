package com.atipera.repoviewer.exception.model;

public record ApiError(
        String path,
        String message,
        int statusCode,
        String timestamp
) {}

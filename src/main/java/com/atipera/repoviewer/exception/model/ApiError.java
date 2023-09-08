package com.atipera.repoviewer.exception.model;

public record ApiError(
        int statusCode,
        String message
) {}

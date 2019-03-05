package com.netflix.exhibitor.core.gcs;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.util.Objects;

public class PropertyBasedGcsCredential implements GcsCredential {

    private final GoogleCredentials credentials;
    private final String project;

    public PropertyBasedGcsCredential(GoogleCredentials credentials, String project) throws IOException {
        Objects.requireNonNull(project, "GCSProject is mandatory");
        this.credentials = credentials;
        this.project = project;
    }

    @Override
    public GoogleCredentials getCredentials() {
        return credentials;
    }

    @Override
    public String getProject() {
        return project;
    }
}
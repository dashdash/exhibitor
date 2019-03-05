package com.netflix.exhibitor.core.gcs;

import com.google.auth.oauth2.GoogleCredentials;

public interface GcsCredential {
    public GoogleCredentials getCredentials();
    public String getProject();
}

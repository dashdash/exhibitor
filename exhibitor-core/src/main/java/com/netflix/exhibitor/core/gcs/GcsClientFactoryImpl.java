package com.netflix.exhibitor.core.gcs;


import com.google.auth.oauth2.GoogleCredentials;

public class GcsClientFactoryImpl implements GcsClientFactory {
    @Override
    public GcsClient makeNewClient(String project) throws Exception {
        return new GcsClientImpl(project);
    }

    @Override
    public GcsClient makeNewClient(String project, GoogleCredentials credentials) throws Exception {
        return new GcsClientImpl(project, credentials);
    }

    @Override
    public GcsClient makeNewClient(String project, GoogleCredentials credentials, int connectTimeout, int readTimeout) throws Exception {
        return new GcsClientImpl(project, credentials, connectTimeout, readTimeout);
    }
}
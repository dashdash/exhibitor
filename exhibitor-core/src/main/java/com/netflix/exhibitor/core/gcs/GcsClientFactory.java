package com.netflix.exhibitor.core.gcs;

import com.google.auth.oauth2.GoogleCredentials;

public interface GcsClientFactory {

    /**
     * Create a client with the given credentials
     *
     * @param project gcp project
     * @return client
     * @throws Exception errors
     */
    public GcsClient makeNewClient(String project) throws Exception;


    /**
     * Create a client with the given credentials
     *
     * @param credentials credentials
     * @param project gcp project
     * @return client
     * @throws Exception errors
     */
    public GcsClient makeNewClient(String project, GoogleCredentials credentials) throws Exception;

    /**
     * Create a client with the given credentials
     *
     * @param project gcp project
     * @param credentials credentials
     * @param connectTimeout establishing connection timeout
     * @param readTimeout request timeout
     * @return client
     * @throws Exception errors
     */
    public GcsClient makeNewClient(String project, GoogleCredentials credentials, int connectTimeout, int readTimeout) throws Exception;

}
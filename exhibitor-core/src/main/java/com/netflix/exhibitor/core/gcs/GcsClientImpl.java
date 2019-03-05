package com.netflix.exhibitor.core.gcs;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class GcsClientImpl implements GcsClient {

    private final static String CONTENT_TYPE = "text/plain";

    private final GoogleCredentials googleCredentials;
    private Storage storage;
    private int connectTimeout = 1000;
    private int readTimeout = 60000;
    private final String project;

    public GcsClientImpl(String project) throws IOException {
        this.googleCredentials = GoogleCredentials.getApplicationDefault();
        this.project = project;
    }

    public GcsClientImpl(String project, GoogleCredentials googleCredentials) {
        this.googleCredentials = googleCredentials;
        this.project = project;
    }

    public GcsClientImpl(String project, GoogleCredentials googleCredentials, int connectTimeout, int readTimeout) {
        this.googleCredentials = googleCredentials;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.project = project;
    }

    @Override
    public synchronized Storage getClient() throws Exception {
        if (storage == null) {
            this.storage = StorageOptions.newBuilder()
                    .setCredentials(googleCredentials)
                    .setTransportOptions(HttpTransportOptions.newBuilder()
                            .setHttpTransportFactory(new HttpTransportOptions.DefaultHttpTransportFactory())
                            .setConnectTimeout(connectTimeout)
                            .setReadTimeout(readTimeout)
                            .build())
                    .setProjectId(project)
                    .build()
                    .getService();
        }
        return storage;
    }

    @Override
    public byte[] getBlobContent(String bucketName, String objectName) throws Exception {
        Blob blob = getClient().get(bucketName, objectName);

        if(blob == null) {
            return new byte[]{};
        } else {
            return blob.getContent();
        }
    }

    @Override
    public Blob getBlob(String bucketName, String objectName) throws Exception {
        return getClient().get(bucketName, objectName);
    }

    @Override
    public Iterable<Blob> listBlobs(String bucketName, String prefix) throws Exception {
        return getClient().list(bucketName, Storage.BlobListOption.prefix(prefix)).iterateAll();
    }

    @Override
    public void putObject(byte[] bytes, String bucketName, String objectName) throws Exception {
        final Blob blob = getClient().get(bucketName, objectName);

        if (blob == null) {
            getClient().create(BlobInfo.newBuilder(bucketName, objectName)
                    .setContentType(CONTENT_TYPE)
                    .build(), bytes);
        } else {
            WritableByteChannel channel = blob.writer();
            channel.write(ByteBuffer.wrap(bytes));
            channel.close();
        }
    }

    @Override
    public void deleteObject(String bucketName, String objectName) throws Exception {
        getClient().delete(bucketName, objectName);
    }

    @Override
    public void close() throws IOException {

    }
}
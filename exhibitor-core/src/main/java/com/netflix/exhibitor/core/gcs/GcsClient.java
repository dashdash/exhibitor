package com.netflix.exhibitor.core.gcs;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

import java.io.Closeable;

public interface GcsClient extends Closeable {
    public Storage getClient() throws Exception;

    public byte[] getBlobContent(String bucketName, String objectName) throws Exception;

    public Blob getBlob(String bucketName, String objectName) throws Exception;

    public Iterable<Blob> listBlobs(String bucketName, String prefix) throws Exception;

    public void putObject(byte[] bytes, String bucketName, String objectName) throws Exception;

    public void deleteObject(String bucketName, String objectName) throws Exception;
}
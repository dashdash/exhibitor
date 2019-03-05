package com.netflix.exhibitor.core.config.gcs;

import com.google.api.client.http.HttpResponseException;
import com.google.cloud.storage.Blob;
import com.netflix.exhibitor.core.config.PseudoLockBase;
import com.netflix.exhibitor.core.gcs.GcsClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GcsPseudoLock extends PseudoLockBase
{
    private final GcsClient client;
    private final String bucketName;

    public static final int HTTP_NOT_FOUND = 404;


    /**
     * @param client the Gcs client
     * @param bucketName the Gcs container
     * @param lockPrefix the Gcs blob prefix
     * @param timeoutMs max age for locks
     * @param pollingMs how often to poll Gcs
     * @param settlingMs how long to wait for Gcs to reach consistency
     */
    public GcsPseudoLock(GcsClient client, String bucketName, String lockPrefix, int timeoutMs, int pollingMs, int settlingMs) {
        super(lockPrefix, timeoutMs, pollingMs, settlingMs);
        this.client = client;
        this.bucketName = bucketName;
    }

    @Override
    protected void createFile(String objectName, byte[] contents) throws Exception {
        client.putObject(contents, bucketName, objectName);
    }

    @Override
    protected void deleteFile(String objectName) throws Exception {
        try {
            client.deleteObject(bucketName, objectName);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() != HTTP_NOT_FOUND) {
                throw e;
            }
        }
    }

    @Override
    protected List<String> getFileNames(String lockPrefix) throws Exception {
        Iterable<Blob> storageObjects = client.listBlobs(bucketName, lockPrefix);
        Iterator<Blob> it = storageObjects.iterator();
        List<String> fileNames = new ArrayList<String>();
        while (it.hasNext()) {
            Blob blob = it.next();
            fileNames.add(blob.getName());
        }

        return fileNames;
    }
}
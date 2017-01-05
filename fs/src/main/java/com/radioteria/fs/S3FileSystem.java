package com.radioteria.fs;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;

public class S3FileSystem implements FileSystem {
    private final AmazonS3Client s3Client;
    private final String bucketName;

    public S3FileSystem(String bucketName, String accessKey, String secretKey) {
        this.bucketName = bucketName;
        this.s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Override
    public boolean has(String path) throws FileSystemException {
        try {
            return s3Client.doesObjectExist(bucketName, path);
        } catch (SdkClientException e) {
            throw new FileSystemException("S3 File System Exception.", e);
        }
    }

    @Override
    public InputStream get(String path) throws FileSystemException {
        return s3Client.getObject(bucketName, path).getObjectContent();
    }

    @Override
    public String getContentType(String path) throws FileSystemException {
        try {
            return s3Client.getObject(bucketName, path).getObjectMetadata().getContentType();
        } catch (SdkClientException e) {
            throw new FileSystemException("S3 File System Exception.", e);
        }
    }

    @Override
    public void put(String path, InputStream sourceStream, String contentType) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        try {
            s3Client.putObject(bucketName, path, sourceStream, metadata);
        } catch (SdkClientException e) {
            throw new FileSystemException("S3 File System Exception.", e);
        }
    }

    @Override
    public void delete(String path) throws FileSystemException {
        try {
            s3Client.deleteObject(bucketName, path);
        } catch (SdkClientException e) {
            throw new FileSystemException("S3 File System Exception.", e);
        }
    }

    @Override
    public FileSystemType getType() {
        return FileSystemType.S3;
    }
}

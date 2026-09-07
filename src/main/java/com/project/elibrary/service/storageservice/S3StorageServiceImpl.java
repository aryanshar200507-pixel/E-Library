package com.project.elibrary.service.storageservice;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

import com.project.elibrary.config.S3Config;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

public class S3StorageServiceImpl implements StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3StorageServiceImpl() {
        this.s3Client = S3Config.getS3Client();
        this.bucketName = S3Config.getBucketName();

        this.s3Presigner = S3Presigner.builder()
                .region(S3Config.getRegion())
                .build();
    }

    @Override
    public String uploadPdf(InputStream inputStream, String fileName, long contentLength) {

        if (inputStream == null) {
            throw new IllegalArgumentException("PDF input stream cannot be null.");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("PDF file name cannot be empty.");
        }

        String storageKey = generateStorageKey("books", fileName);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(inputStream, contentLength)
        );

        return storageKey;
    }

    @Override
    public String uploadCover(InputStream inputStream, String fileName, long contentLength) {

        if (inputStream == null) {
            throw new IllegalArgumentException("Cover input stream cannot be null.");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Cover file name cannot be empty.");
        }

        String storageKey = generateStorageKey("covers", fileName);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .contentType(getCoverContentType(fileName))
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(inputStream, contentLength)
        );

        return storageKey;
    }

    @Override
    public boolean deleteFile(String storageKey) {

        if (storageKey == null || storageKey.isBlank()) {
            return false;
        }

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();

        s3Client.deleteObject(request);

        return true;
    }

    @Override
    public String getFileUrl(String storageKey) {

        if (storageKey == null || storageKey.isBlank()) {
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(30))
                        .getObjectRequest(getObjectRequest)
                        .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }

    private String generateStorageKey(String folder, String fileName) {

        String safeFileName =
                fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        return folder + "/" + UUID.randomUUID() + "-" + safeFileName;
    }

    private String getCoverContentType(String fileName) {

        String lowerCaseFileName = fileName.toLowerCase();

        if (lowerCaseFileName.endsWith(".png")) {
            return "image/png";
        }

        if (lowerCaseFileName.endsWith(".webp")) {
            return "image/webp";
        }

        return "image/jpeg";
    }
}
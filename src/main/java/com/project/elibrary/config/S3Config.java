package com.project.elibrary.config;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Config {
	private static final String BUCKET_NAME = "elibrary-book";
	private static final Region REGION = Region.US_EAST_1;

	private S3Config() {
	}

	public static S3Client getS3Client() {
		return S3Client.builder().region(REGION).credentialsProvider(DefaultCredentialsProvider.create()).build();
	}

	public static String getBucketName() {
		return BUCKET_NAME;
	}

	public static Region getRegion() {
		return REGION;
	}
}

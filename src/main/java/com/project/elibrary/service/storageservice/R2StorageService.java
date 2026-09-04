package com.project.elibrary.service.storageservice;

import java.io.InputStream;

public interface R2StorageService {
	String uploadPdf(InputStream inputStream, String fileName);

	String uploadCover(InputStream inputStream, String fileName);

	boolean deleteFile(String storageKey);
}

package com.project.elibrary.service.storageservice;

import java.io.InputStream;

public interface StorageService {
	String uploadPdf(InputStream inputStream, String fileName , long contentLength);

	String uploadCover(InputStream inputStream, String fileName , long contentLength);

	boolean deleteFile(String storageKey);
	
	String getFileUrl(String storageKey);
}

package com.cjsrhd94.S3.S3;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class S3Controller {

	private final S3Service s3Service;

	/** 파일 업로드 */
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {
		String filePath = "static";
		return s3Service.upload(file, filePath);
	}

	/** 파일 다운로드 */
	@PostMapping("/download")
	public byte[] download(String fileUrl) throws IOException {
		return s3Service.download(fileUrl);
	}

	/** 파일 삭제 */
	@DeleteMapping
	public String delete(String fileUrl) {
		return s3Service.delete(fileUrl);
	}

}

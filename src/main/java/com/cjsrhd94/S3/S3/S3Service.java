package com.cjsrhd94.S3.S3;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
public class S3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client amazonS3Client;

	public String upload(MultipartFile file, String dirPath) throws IOException {
		String fileName = file.getOriginalFilename();
		String filePath = dirPath + "/" + UUID.randomUUID()
			.toString().concat(extractFileExtension(fileName));

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());

		amazonS3Client.putObject(new PutObjectRequest(bucket, filePath, file.getInputStream(), objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));

		return "/" + filePath;
	}

	private String extractFileExtension(String imgName) {
		return imgName.substring(imgName.lastIndexOf("."));
	}

	public byte[] download(String fileUrl) throws IOException {
		S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileUrl));
		S3ObjectInputStream objectInputStream = object.getObjectContent();
		return IOUtils.toByteArray(objectInputStream);
	}

	public String delete(String fileUrl) {
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileUrl));
		return "/" + fileUrl;
	}
}

package com.example.skillforge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String cleanFileName = UUID.randomUUID() + "-" + (file.getOriginalFilename() != null ? file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_") : "file.bin");
        String s3Key = folder + "/" + cleanFileName;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;
        } catch (Exception e) {
            // Local storage fallback for local development
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads", folder).toAbsolutePath().normalize();
            java.nio.file.Files.createDirectories(uploadPath);
            java.nio.file.Path targetLocation = uploadPath.resolve(cleanFileName);
            java.nio.file.Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/api/files/" + folder + "/" + cleanFileName;
        }
    }

    public void deleteFile(String url) {
        if (url == null) return;

        try {
            if (url.contains(".com/")) {
                String key = url.substring(url.indexOf(".com/") + 5);
                DeleteObjectRequest request = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();
                s3Client.deleteObject(request);
            }
        } catch (Exception ignored) {
        }
    }

    public String uploadProfileImage(MultipartFile file, Long userId) throws IOException {
        String fileName = "profile-" + userId + "-" + System.currentTimeMillis() + ".jpg";
        try {
            String s3Key = "profile-images/" + userId + "/" + fileName;
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;
        } catch (Exception e) {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads", "profile-images").toAbsolutePath().normalize();
            java.nio.file.Files.createDirectories(uploadPath);
            java.nio.file.Path targetLocation = uploadPath.resolve(fileName);
            java.nio.file.Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/api/files/profile-images/" + fileName;
        }
    }

    public String uploadBannerImage(MultipartFile file, Long userId) throws IOException {
        String fileName = "banner-" + userId + "-" + System.currentTimeMillis() + ".jpg";
        try {
            String s3Key = "banners/" + userId + "/" + fileName;
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;
        } catch (Exception e) {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads", "banners").toAbsolutePath().normalize();
            java.nio.file.Files.createDirectories(uploadPath);
            java.nio.file.Path targetLocation = uploadPath.resolve(fileName);
            java.nio.file.Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/api/files/banners/" + fileName;
        }
    }
}


package com.codestates.edusync.model.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3 이미지 저장 로직
     * @param file 이미지 파일
     * @param bucketPath S3 이미지 저장 Bucket
     * @return 이미지 저장 주소
     */
    public String uploadImage(MultipartFile file, String bucketPath) {
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            uploadFile(inputStream, objectMetadata, fileName, bucket.concat(bucketPath));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        return getFileUrl(fileName, bucket.concat(bucketPath));
    }

    /**
     * S3에 저장할 파일명
     * @param originalFileName 원본 파일명
     * @return S3에 저장할 파일명
     */
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    /**
     * 파일 포맷 유효성 검사
     * @param fileName file name
     * @return 파일명
     */
    private String getFileExtension(String fileName) {

        String extension;

        try {
            extension = fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }

        if (!extension.equals(".jpg") && !extension.equals(".png")) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }

        return extension;
    }

    /**
     * S3에 이미지 업로드
     * @param inputStream InputStream
     * @param objectMetadata Object metadata
     * @param fileName File name
     */
    public void uploadFile(InputStream inputStream,
                           ObjectMetadata objectMetadata,
                           String fileName,
                           String bucketPath) {

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketPath, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);

    }

    /**
     * 이미지 저장 경로
     * @param fileName 파일명
     * @param bucketPath S3 저장 Bucket
     * @return 이미지 저장 경로
     */
    public String getFileUrl(String fileName, String bucketPath) {
        return amazonS3.getUrl(bucketPath, fileName).toString();
    }
}
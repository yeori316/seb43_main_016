package com.codestates.edusync.model.common.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface AwsS3ServiceInterface {

    /**
     * S3 이미지 저장 로직
     * @param file       이미지 파일
     * @param bucketPath S3 이미지 저장 Bucket
     * @return 이미지 저장 주소
     */
    String uploadImage(MultipartFile file, String bucketPath);

    /**
     * S3에 저장할 파일명
     * @param originalFileName 원본 파일명
     * @return S3에 저장할 파일명
     */
    String createFileName(String originalFileName);

    /**
     * 파일 포맷 유효성 검사
     * @param fileName file name
     * @return 파일명
     */
    String getFileExtension(String fileName);

    /**
     * S3에 이미지 업로드
     * @param inputStream    InputStream
     * @param objectMetadata Object metadata
     * @param fileName       File name
     */
    void uploadFile(InputStream inputStream,
                    ObjectMetadata objectMetadata,
                    String fileName,
                    String bucketPath);

    /**
     * 이미지 저장 경로
     * @param fileName   파일명
     * @param bucketPath S3 저장 Bucket
     * @return 이미지 저장 경로
     */
    String getFileUrl(String fileName, String bucketPath);
}

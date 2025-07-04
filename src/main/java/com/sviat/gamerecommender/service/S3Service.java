package com.sviat.gamerecommender.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@Service
public class S3Service {

    private static final String GAMES_PATH_FORMAT = "games/%s/%s.%s";
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public enum GameImageType {
        ORIGINAL("original", 450, 600),  // Good size for game cards
        THUMBNAIL("thumbnail", 225, 300); // Half size for grid views

        private final String prefix;
        private final int width;
        private final int height;

        GameImageType(String prefix, int width, int height) {
            this.prefix = prefix;
            this.width = width;
            this.height = height;
        }

        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }

    public GameImageUrls uploadGameImage(MultipartFile file, String gameId) throws IOException {
        Objects.requireNonNull(file, "File cannot be null");
        Objects.requireNonNull(gameId, "Game ID cannot be null");
        
        String extension = getFileExtension(file.getOriginalFilename());
        
        // Upload original image
        String originalKey = String.format(GAMES_PATH_FORMAT, gameId, GameImageType.ORIGINAL.prefix, extension);
        uploadToS3(file.getInputStream(), originalKey, file.getContentType(), file.getSize());
        
        // Generate and upload thumbnail
        String thumbnailKey = String.format(GAMES_PATH_FORMAT, gameId, GameImageType.THUMBNAIL.prefix, extension);
        try (var thumbnailOutput = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(GameImageType.THUMBNAIL.width, GameImageType.THUMBNAIL.height)
                    .keepAspectRatio(true)
                    .crop(Positions.CENTER)
                    .outputQuality(0.85)
                    .outputFormat(extension)
                    .toOutputStream(thumbnailOutput);
            
            byte[] thumbnailBytes = thumbnailOutput.toByteArray();
            uploadToS3(new ByteArrayInputStream(thumbnailBytes), thumbnailKey, file.getContentType(), thumbnailBytes.length);
        }
        
        return new GameImageUrls(getImageUrl(originalKey), getImageUrl(thumbnailKey));
    }
    
    private void uploadToS3(InputStream inputStream, String key, String contentType, long contentLength) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
    }
    
    public record GameImageUrls(String originalUrl, String thumbnailUrl) {}
    
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    public void deleteGameImage(String imageKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(imageKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public String getImageUrl(String key) {
        var request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        return s3Client.utilities().getUrl(request).toString();
    }
}

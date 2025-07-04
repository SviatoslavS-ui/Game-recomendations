package com.sviat.gamerecommender.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class S3ServiceTest {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;    
   
    @Test
    public void cleanupBucket() {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("games/test-game-")
                .build();
        
        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
        listRes.contents().forEach(obj -> s3Service.deleteGameImage(obj.key()));
    }

    @Test
    public void testS3Operations() throws Exception {
        // 1. Initial check - list objects
        ListObjectsV2Response initialList = listObjects();
        int initialCount = initialList.keyCount();
        
        // 2. Upload a test file
        // Load the Baldur's Gate 3 test image
        String gameId = "baldurs-gate-3";
        MockMultipartFile testFile = new MockMultipartFile(
            "bg3_cover.png",
            "bg3_cover.png",
            "image/png",
            getClass().getResourceAsStream("/test-images/bg3_cover.png")
        );        
        S3Service.GameImageUrls urls = s3Service.uploadGameImage(testFile, gameId);
        assertNotNull(urls.originalUrl(), "Upload should return a valid original URL");
        assertNotNull(urls.thumbnailUrl(), "Upload should return a valid thumbnail URL");
        
        // Verify URL paths follow our convention
        assertTrue(urls.originalUrl().contains("games/" + gameId + "/original"), "Original URL should contain correct path");
        assertTrue(urls.thumbnailUrl().contains("games/" + gameId + "/thumbnail"), "Thumbnail URL should contain correct path");
        
        // 3. Verify both files were uploaded
        ListObjectsV2Response afterUploadList = listObjects();
        assertEquals(initialCount + 2, afterUploadList.keyCount(), "Object count should increase by 2 (original + thumbnail)");
        
        // 4. Clean up - delete both files
        String originalKey = urls.originalUrl().substring(urls.originalUrl().indexOf("/games/") + 1);
        String thumbnailKey = urls.thumbnailUrl().substring(urls.thumbnailUrl().indexOf("/games/") + 1);
        s3Service.deleteGameImage(originalKey);
        s3Service.deleteGameImage(thumbnailKey);
        
        // 5. Verify deletion
        ListObjectsV2Response finalList = listObjects();
        assertEquals(initialCount, finalList.keyCount(), "Object count should return to initial value");
    }
    
    private ListObjectsV2Response listObjects() {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        return s3Client.listObjectsV2(listReq);
    }
}
package com.sviat.gamerecommender.service;

import com.sviat.gamerecommender.model.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class S3ServiceTest {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private JsonService jsonService;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Test
    @Tag("manual")
    @DisplayName("Cleanup bucket (run manually)")
    public void cleanupBucket() {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                // .prefix("games/test-game-")
                .build();

        s3Client.listObjectsV2(listReq)
                .contents()
                .forEach(obj -> s3Service.deleteGameImage(obj.key()));
    }

    @Test
    @Tag("manual")
    @DisplayName("Upload game images from local directory (run manually)")
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
                getClass().getResourceAsStream("/test-images/bg3_cover.png"));
        S3Service.GameImageUrls urls = s3Service.uploadGameImage(testFile, gameId);

        assertAll("Game image upload test", () -> {
            assertNotNull(urls.originalUrl(), "Upload should return a valid original URL");
            assertNotNull(urls.thumbnailUrl(), "Upload should return a valid thumbnail URL");
        });

        // Verify URL paths follow our convention
        assertAll("Game image URL test", () -> {
            assertTrue(urls.originalUrl().contains("games/" + gameId + "/original"),
                    "Original URL should contain correct path");
            assertTrue(urls.thumbnailUrl().contains("games/" + gameId + "/thumbnail"),
                    "Thumbnail URL should contain correct path");
        });

        // 3. Verify both files were uploaded
        ListObjectsV2Response afterUploadList = listObjects();
        assertEquals(initialCount + 2, afterUploadList.keyCount(),
                "Object count should increase by 2 (original + thumbnail)");

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

    @Test
    @Tag("manual")
    @Tag("game-details")
    @DisplayName("Test game details upload and retrieval")
    public void testGameDetailsUploadAndRetrieval() throws IOException {
        // 1. Generate a unique game ID for testing
        String gameId = "test-game-" + UUID.randomUUID().toString().substring(0, 8);

        // 2. Use simple HTML content from TestGameData
        String detailsContent = TestGameData.SIMPLE_GAME_DETAILS_HTML;

        try {
            // 3. Upload the game details
            String detailsUrl = s3Service.uploadGameDetails(gameId, detailsContent);

            // 4. Verify the URL is not null and contains the expected path
            assertAll("Game details upload test", () -> {
                assertNotNull(detailsUrl, "Upload should return a valid URL");
                assertTrue(detailsUrl.contains("games/" + gameId + "/details.html"),
                        "Details URL should contain the correct path");
            });
            // 5. Retrieve the uploaded content
            String retrievedContent = s3Service.getGameDetails(gameId);
            // 6. Verify the retrieved content matches what we uploaded
            assertAll("Game details retrieval test", () -> {
                assertNotNull(retrievedContent, "Retrieved content should not be null");
                assertFalse(retrievedContent.isEmpty(), "Retrieved content should not be empty");
                assertEquals(detailsContent, retrievedContent, "Retrieved content should match uploaded content");
            });

            // 7. Clean up - delete the test details
            s3Service.deleteGameDetails(gameId);

            // 8. Verify deletion by trying to retrieve again
            String afterDeleteContent = s3Service.getGameDetails(gameId);
            assertTrue(afterDeleteContent.isEmpty(), "Content should be empty after deletion");

            System.out.println("Successfully tested game details upload and retrieval for game ID: " + gameId);
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    @Tag("manual")
    @Tag("game-details")
    @DisplayName("Upload game details from sample file (run manually)")
    public void testUploadGameDetailsFromFile() throws IOException {
        // Path to a sample HTML file containing game details
        Path sampleFilePath = Paths.get("src/test/resources/sample-game-details.html");

        // Check if the file exists
        if (!Files.exists(sampleFilePath)) {
            System.out.println("Sample game details file not found at: " + sampleFilePath);
            fail("Required test file not found: " + sampleFilePath);
            return;
        }
        
        System.out.println("Using sample game details file at: " + sampleFilePath);

        // Read the file content
        String fileContent = Files.readString(sampleFilePath);

        // Upload to S3 for a specific game
        String gameId = "baldurs-gate-3";
        String detailsUrl = s3Service.uploadGameDetails(gameId, fileContent);

        System.out.println("Uploaded game details for " + gameId);
        System.out.println("Details URL: " + detailsUrl);

        // Verify retrieval
        String retrievedContent = s3Service.getGameDetails(gameId);
        boolean contentMatches = fileContent.equals(retrievedContent);

        System.out.println("Retrieved content matches original: " + contentMatches);
        System.out.println("Content length: " + retrievedContent.length() + " characters");
    }

    @Test
    @Tag("manual")
    @DisplayName("Upload game images from local directory (run manually)")
    public void uploadGameImagesFromLocalDirectory() throws IOException {
        // Path to the local images directory
        String localImagesPath = "C:\\Users\\svyat\\Pictures\\Screenshots\\cards";
        Path imagesDirectory = Paths.get(localImagesPath);

        // Check if directory exists
        if (!Files.exists(imagesDirectory)) {
            System.out.println("Images directory does not exist: " + localImagesPath);
            return;
        }

        // Load games using JsonService
        List<Game> games = jsonService.loadFromFile(
                "src/main/resources/data/games.json",
                new TypeReference<List<Game>>() {
                });
        System.out.println("Loaded " + games.size() + " games from JSON");

        // List to store updated games
        List<Game> updatedGames = new ArrayList<>();

        // Get all .jpg image files from the directory
        File[] imageFiles = new File(localImagesPath).listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No .jpg image files found in directory: " + localImagesPath);
            return;
        }
        System.out.println("Found " + imageFiles.length + " .jpg image files");

        // Process each game and try to find matching image
        for (Game game : games) {
            String gameId = game.getId();
            String gameTitle = game.getTitle();

            // Look for image file that matches gameId exactly
            File imageFile = new File(localImagesPath, gameId + ".jpg");

            if (imageFile.exists()) {
                try {
                    System.out.println("Uploading image for game: " + gameTitle + " -> " + imageFile.getName());

                    // Create MockMultipartFile from the actual file
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

                    MockMultipartFile multipartFile = new MockMultipartFile(
                            "file",
                            imageFile.getName(),
                            "image/jpeg",
                            imageBytes);

                    // Upload to S3
                    S3Service.GameImageUrls urls = s3Service.uploadGameImage(multipartFile, gameId);

                    System.out.println("  Original URL: " + urls.originalUrl());
                    System.out.println("  Thumbnail URL: " + urls.thumbnailUrl());

                    // Create updated game object with S3 URLs
                    Game updatedGame = Game.builder()
                            .id(game.getId())
                            .title(game.getTitle())
                            .description(game.getDescription())
                            .imageUrl(urls.originalUrl())
                            .thumbnailUrl(urls.thumbnailUrl())
                            .developer(game.getDeveloper())
                            .publisher(game.getPublisher())
                            .genres(game.getGenres())
                            .tags(game.getTags())
                            .metacriticScore(game.getMetacriticScore())
                            .ageRating(game.getAgeRating())
                            .userScore(game.getUserScore())
                            .reviewCount(game.getReviewCount())
                            .platforms(game.getPlatforms())
                            .price(game.getPrice())
                            .isMultiplayer(game.isMultiplayer())
                            .playtimeHours(game.getPlaytimeHours())
                            .build();

                    updatedGames.add(updatedGame);

                } catch (Exception e) {
                    System.err.println("Failed to upload image for game: " + gameTitle + " - " + e.getMessage());
                }
            } else {
                System.out.println("No matching image found for game: " + gameTitle + " (ID: " + gameId + ")");

                // Create game object with placeholder image URL
                String placeholderUrl = "/images/placeholder-game.jpg";
                Game updatedGame = Game.builder()
                        .id(game.getId())
                        .title(game.getTitle())
                        .description(game.getDescription())
                        .imageUrl(placeholderUrl)
                        .thumbnailUrl(placeholderUrl)
                        .developer(game.getDeveloper())
                        .publisher(game.getPublisher())
                        .genres(game.getGenres())
                        .tags(game.getTags())
                        .metacriticScore(game.getMetacriticScore())
                        .ageRating(game.getAgeRating())
                        .userScore(game.getUserScore())
                        .reviewCount(game.getReviewCount())
                        .platforms(game.getPlatforms())
                        .price(game.getPrice())
                        .isMultiplayer(game.isMultiplayer())
                        .playtimeHours(game.getPlaytimeHours())
                        .build();

                updatedGames.add(updatedGame);
                System.out.println("  Set placeholder image URL: " + placeholderUrl);
            }
        }

        // Save updated games back to JSON file
        try {
            jsonService.saveToFile("src/main/resources/data/games.json", updatedGames);
            System.out.println("Successfully updated games.json with " + updatedGames.size() + " games");
        } catch (Exception e) {
            System.err.println("Failed to save updated games to JSON file: " + e.getMessage());
        }
        System.out.println("Game image upload process completed");
    }
}
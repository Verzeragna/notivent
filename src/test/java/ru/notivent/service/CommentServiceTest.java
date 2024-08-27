package ru.notivent.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.notivent.dto.CommentPostDto;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class CommentServiceTest {

    static DockerImageName postgisImage =
            DockerImageName.parse("postgis/postgis:16-3.4-alpine").asCompatibleSubstituteFor("postgres");

    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(postgisImage)
                    .withDatabaseName("notivent")
                    .withUsername("postgres")
                    .withPassword("05konrad05");

    @Autowired
    CommentService commentService;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void getComments() {
        var result = commentService.getComments(UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));
        var comments = result.getBody();

        assertFalse(comments.isEmpty());

        var comment = comments.get(0);

        assertDoesNotThrow(comment::getUuid);
        assertDoesNotThrow(comment::getText);
        assertDoesNotThrow(comment::getNickName);
        assertDoesNotThrow(comment::getCreatedAt);
      }

    @Test
    void create() {
        var commentPost = new CommentPostDto(UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"), "Nice place for chilling!!!!", Instant.now());
        commentService.create(UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"), commentPost);
        var result = commentService.getComments(UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));
        var comments = result.getBody();

        assertFalse(comments.isEmpty());
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Nice place for chilling!!!!")));
      }

    @Test
    void getGeoPointCommentsCount() {
        var result = commentService.getGeoPointCommentsCount(UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));
        var commentsCount = result.getBody();

        assertEquals(1, commentsCount);
      }
}
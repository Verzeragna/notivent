package ru.notivent.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.notivent.dto.AuthDto;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class AuthenticationServiceTest {

  static DockerImageName postgisImage =
      DockerImageName.parse("postgis/postgis:16-3.4-alpine").asCompatibleSubstituteFor("postgres");

  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(postgisImage)
          .withDatabaseName("notivent")
          .withUsername("postgres")
          .withPassword("05konrad05");

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @Autowired AuthenticationService authenticationService;

  @Test
  void testAuthRegister_new_user_OK() {
    var authDto = new AuthDto("dGVzdEBtYWlsLnJ1", "cGFzc3dvcmQ="); // password: password

    var response = authenticationService.authRegister(authDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testAuthRegister_Error() {
    var authDto = new AuthDto("dGVzdDFAbWFpbC5ydQ==", "cGFzc3dvcmQ="); // password: password

    var response = authenticationService.authRegister(authDto);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testAuthRegister_existing_user_OK() {
    var authDto = new AuthDto("dGVzdDFAbWFpbC5ydQ==", "cGFzc3dvcmRwYXNzd29yZA=="); // password: password

    var response = authenticationService.authRegister(authDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}

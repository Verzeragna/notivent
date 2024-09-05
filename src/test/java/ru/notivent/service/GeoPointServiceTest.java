package ru.notivent.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.dto.LocationDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.enums.GeoPointType;
import ru.notivent.enums.GradeType;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GeoPointServiceTest {

  static DockerImageName postgisImage =
      DockerImageName.parse("postgis/postgis:16-3.4-alpine").asCompatibleSubstituteFor("postgres");

  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(postgisImage)
          .withDatabaseName("notivent")
          .withUsername("postgres")
          .withPassword("05konrad05");

  @Autowired GeoPointService geoPointService;

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @Test
  @Order(1)
  void getAllGeoPointsForUser() {
    var userGeoPointDto = new UserGeoPointDto(54.79591645758609, 55.955214500426246);
    var points =
        geoPointService.getAllGeoPointsForUser(
            userGeoPointDto, UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"));

    assertFalse(points.getPoints().isEmpty());
  }

  @Test
  @Order(2)
  void createGeoPoint_NoImages_OK() {
    var location = new LocationDto();
    location.setPostalCode("450007");
    location.setCountry("Россия");
    location.setArea("Респ. Башкортостан");
    location.setCity("Уфа");
    location.setStreet("ул. Мухаметгалея Искужина");
    location.setHouseNumber("10");
    location.setAddressLine("ул. Мухаметгалея Искужина,10,Уфа,Респ. Башкортостан,450007");
    var geoPointDto = new GeoPointDto();
    geoPointDto.setType(GeoPointType.PRIVATE);
    geoPointDto.setLatitude(54.79438871577587);
    geoPointDto.setLongitude(55.95339059829712);
    geoPointDto.setLocation(location);
    geoPointDto.setUserLatitude(54.79438871577586);
    geoPointDto.setUserLongitude(55.95339059829711);
    geoPointDto.setName("Поле ромашек");
    geoPointDto.setDescription("Оно реально существует!");
    geoPointDto.setCreatedAt(Instant.now());
    geoPointDto.setGrade(0);

    var result =
        geoPointService.createGeoPoint(
            geoPointDto, UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"));

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  @Order(4)
  void createGeoPoint_NOT_ACCEPTABLE() {
    var location = new LocationDto();
    location.setPostalCode("450007");
    location.setCountry("Россия");
    location.setArea("Респ. Башкортостан");
    location.setCity("Уфа");
    location.setStreet("ул. Мухаметгалея Искужина");
    location.setHouseNumber("10");
    location.setAddressLine("ул. Мухаметгалея Искужина,10,Уфа,Респ. Башкортостан,450007");
    var geoPointDto = new GeoPointDto();
    geoPointDto.setType(GeoPointType.PUBLIC);
    geoPointDto.setLatitude(54.79438871577587);
    geoPointDto.setLongitude(55.95339059829712);
    geoPointDto.setLocation(location);
    geoPointDto.setUserLatitude(54.79438871577586);
    geoPointDto.setUserLongitude(55.95339059829711);
    geoPointDto.setName("Поле ромашек");
    geoPointDto.setDescription("Оно реально существует!");
    geoPointDto.setCreatedAt(Instant.now());
    geoPointDto.setGrade(0);

    var result =
        geoPointService.createGeoPoint(
            geoPointDto, UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"));

    assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
  }

  @Test
  @Order(5)
  void createGeoPoint_EXPECTATION_FAILED() {
    var location = new LocationDto();
    location.setPostalCode("450007");
    location.setCountry("Россия");
    location.setArea("Респ. Башкортостан");
    location.setCity("Уфа");
    location.setStreet("ул. Мухаметгалея Искужина");
    location.setHouseNumber("10");
    location.setAddressLine("ул. Мухаметгалея Искужина,10,Уфа,Респ. Башкортостан,450007");
    var geoPointDto = new GeoPointDto();
    geoPointDto.setType(GeoPointType.PRIVATE);
    geoPointDto.setLatitude(54.79438871577587);
    geoPointDto.setLongitude(55.95339059829712);
    geoPointDto.setLocation(location);
    geoPointDto.setUserLatitude(54.77438871577586);
    geoPointDto.setUserLongitude(55.93339059829711);
    geoPointDto.setName("Поле ромашек");
    geoPointDto.setDescription("Оно реально существует!");
    geoPointDto.setCreatedAt(Instant.now());
    geoPointDto.setGrade(0);

    var result =
        geoPointService.createGeoPoint(
            geoPointDto, UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"));

    assertEquals(HttpStatus.EXPECTATION_FAILED, result.getStatusCode());
  }

  @Test
  @Order(6)
  void findGeoPointById_OK() {
    var result =
        geoPointService.findGeoPointById(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  @Order(7)
  void findGeoPointById_NOT_ACCEPTABLE() {
    var result =
        geoPointService.findGeoPointById(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("a9346b4b-00c4-4438-96a6-5d6fb9bf7eb9"));

    assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
  }

  @Test
  @Order(8)
  void findGeoPointById_BAD_REQUEST() {
    var result =
        geoPointService.findGeoPointById(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("a9346b4b-00c4-4438-96a6-4e48eb39e900"));

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  @Order(9)
  void isGeoPointBelongUser_TRUE() {
    var result =
        geoPointService.isGeoPointBelongUser(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));

    assertTrue(result);
  }

  @Test
  @Order(10)
  void isGeoPointBelongUser_FALSE() {
    var result =
        geoPointService.isGeoPointBelongUser(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e911"),
            UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));

    assertFalse(result);
  }

  @Test
  @Order(11)
  void setGeoPointGrade_PLUS() {
    var result =
        geoPointService.setGeoPointGrade(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"),
            GradeType.PLUS);

    assertEquals(1, result.getBody());
  }

  @Test
  @Order(12)
  void setGeoPointGrade_MINUS() {
    var result =
        geoPointService.setGeoPointGrade(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("a9346b4b-00c4-4438-96a6-5d6fb9bf7eb9"),
            GradeType.MINUS);

    assertEquals(2, result.getBody());
  }

  @Test
  @Order(13)
  void getGeoPointGrade() {
    var result =
        geoPointService.getGeoPointGrade(UUID.fromString("834955b7-c8aa-43fe-a47e-54f7597ba671"));

    assertEquals(1, result.getBody());
  }

  @Test
  @Order(14)
  void deleteGeoPoint() {
    geoPointService.deleteGeoPoint(
        UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
        UUID.fromString("a9346b4b-00c4-4438-96a6-5d6fb9bf7eb9"));
    var result =
        geoPointService.findGeoPointById(
            UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900"),
            UUID.fromString("a9346b4b-00c4-4438-96a6-5d6fb9bf7eb9"));

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }
}

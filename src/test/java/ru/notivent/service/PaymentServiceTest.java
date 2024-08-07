package ru.notivent.service;

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
import ru.notivent.dao.OrderDao;
import ru.notivent.dto.CreateOrderDto;
import ru.notivent.dto.UpdateOrderDto;
import ru.notivent.enums.OrderStatus;
import ru.notivent.model.Order;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class PaymentServiceTest {
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

    @Autowired
    PaymentService paymentService;
    @Autowired
    OrderDao orderDao;
    @Autowired
    SubscriptionService subscriptionService;

    @Test
    void testCreateOrder_User_Not_Found() {
        var userId = UUID.randomUUID();

        var result = paymentService.createOrder(userId, null);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void testCreateOrder_Tariff_Not_Found() {
        var orderDto = new CreateOrderDto(UUID.fromString("fa9b4c8b-8e4c-4a62-a5ee-bcf87943c3af"), OffsetDateTime.now());
        var userId = UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900");

        var result = paymentService.createOrder(userId, orderDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testCreateOrder_Ok() {
        var orderDto = new CreateOrderDto(UUID.fromString("a7b73ce0-015e-471f-9725-02b05a710ea1"), OffsetDateTime.now());
        var userId = UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900");

        var result = paymentService.createOrder(userId, orderDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        var order = orderDao.findById(result.getBody().getOrder().getOrderId());

        assertTrue(order.isPresent());
    }

    @Test
    void testUpdateOrder_User_Not_Found() {
        var userId = UUID.randomUUID();

        var result = paymentService.updateOrder(userId, null);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void testUpdateOrder_Order_Not_Found() {
        var orderDto = new UpdateOrderDto(666, UUID.fromString("fa9b4c8b-8e4c-4a62-a5ee-bcf87943c3af"), OrderStatus.ERROR, OffsetDateTime.now());
        var userId = UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900");

        var result = paymentService.updateOrder(userId, orderDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testUpdateOrder_Ok_Status_Error() {
        var userId = UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900");
        var order = orderDao.create(Order.builder().tariffId(UUID.fromString("a7b73ce0-015e-471f-9725-02b05a710ea1")).status(OrderStatus.PENDING).userId(userId).createdAt(OffsetDateTime.now()).build());
        var orderDto = new UpdateOrderDto(order.getId(), UUID.fromString("a7b73ce0-015e-471f-9725-02b05a710ea1"), OrderStatus.ERROR, OffsetDateTime.now());

        var result = paymentService.updateOrder(userId, orderDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testUpdateOrder_Ok_Status_Done() {
        var userId = UUID.fromString("1751ba42-3936-4284-bd2f-4e48eb39e900");
        var order = orderDao.create(Order.builder().tariffId(UUID.fromString("a7b73ce0-015e-471f-9725-02b05a710ea1")).status(OrderStatus.PENDING).userId(userId).createdAt(OffsetDateTime.now()).build());
        var orderDto = new UpdateOrderDto(order.getId(), UUID.fromString("a7b73ce0-015e-471f-9725-02b05a710ea1"), OrderStatus.DONE, OffsetDateTime.now());

        var result = paymentService.updateOrder(userId, orderDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        var subscription = subscriptionService.findByUserUuid(userId);

        assertTrue(subscription.isPresent());
    }
}

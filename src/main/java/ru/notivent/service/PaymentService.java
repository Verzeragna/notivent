package ru.notivent.service;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.notivent.dao.OrderDao;
import ru.notivent.dao.PaymentParametersDao;
import ru.notivent.dto.CreateOrderDto;
import ru.notivent.dto.OrderDto;
import ru.notivent.dto.PaymentDto;
import ru.notivent.dto.TariffDto;
import ru.notivent.dto.UpdateOrderDto;
import ru.notivent.enums.OrderStatus;
import ru.notivent.model.Order;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

  final PaymentParametersDao paymentParametersDao;
  final OrderDao orderDao;
  final TariffService tariffService;
  final UserService userService;
  final SubscriptionService subscriptionService;

  public ResponseEntity<PaymentDto> createOrder(UUID userUuid, CreateOrderDto createOrderDto) {
    var userOpt = userService.findById(userUuid);
    if (userOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    var user = userOpt.get();
    var tariffOpt = tariffService.findById(createOrderDto.getTariffId());
    if (tariffOpt.isEmpty()) {
      log.error("Tariff {} not found!", createOrderDto.getTariffId());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var tariff = tariffOpt.get();
    var paymentParametersOpt = paymentParametersDao.find();
    if (paymentParametersOpt.isEmpty()) {
      log.error("Payment parameters not found!");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var paymentParameters = paymentParametersOpt.get();
    var order =
        orderDao.create(buildOrder(userUuid, tariff.getUuid(), createOrderDto.getCreatedAt()));
    return ResponseEntity.ok(
        PaymentDto.builder()
            .userEmail(user.getLogin())
            .terminalKey(paymentParameters.getTerminalKey())
            .publicKey(paymentParameters.getPublicKey())
            .order(new OrderDto(order.getId(), tariff.getPrice()))
            .build());
  }

  private Order buildOrder(UUID userId, UUID tariffId, OffsetDateTime createdAt) {
    return Order.builder()
        .userId(userId)
        .tariffId(tariffId)
        .createdAt(createdAt)
        .status(OrderStatus.PENDING)
        .build();
  }

  @Transactional
  public ResponseEntity<Void> updateOrder(UUID userUuid, UpdateOrderDto updateOrderDto) {
    var userOpt = userService.findById(userUuid);
    if (userOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    var orderOpt = orderDao.findById(updateOrderDto.getOrderId());
    if (orderOpt.isEmpty()) {
      log.error("Order {} not found!", updateOrderDto.getOrderId());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var order = orderOpt.get();
    orderDao.updateStatusById(order.getId(), updateOrderDto.getStatus(), updateOrderDto.getUpdatedAt());
    if (updateOrderDto.getStatus().equals(OrderStatus.DONE)) {
      var currentSubscriptionOpt = subscriptionService.findByUserUuid(userUuid);
      if (currentSubscriptionOpt.isEmpty()) {
        subscriptionService.subscribe(
                userUuid,
                TariffDto.builder().uuid(updateOrderDto.getTariffId()).build(),
                updateOrderDto.getUpdatedAt());
        var subscriptionOpt = subscriptionService.findByUserUuid(userUuid);
        var subscription = subscriptionOpt.get();
        orderDao.updateSubscription(order.getId(), subscription.getUuid());
      } else {
        var currentSubscription = currentSubscriptionOpt.get();
        subscriptionService.updateSubscription(currentSubscription, currentSubscription.getEndAt());
        orderDao.updateSubscription(order.getId(), currentSubscription.getUuid());
      }
    }
    return ResponseEntity.ok().build();
  }
}

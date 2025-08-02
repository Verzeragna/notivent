package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.X_AUTH;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.CreateOrderDto;
import ru.notivent.dto.PaymentDto;
import ru.notivent.dto.UpdateOrderDto;
import ru.notivent.service.PaymentService;

@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

  final PaymentService paymentService;

  @PostMapping("create/order")
  public ResponseEntity<PaymentDto> createOrder(
      @RequestHeader(X_AUTH) UUID userUuid, @Valid @RequestBody CreateOrderDto createOrderDto) {
    return paymentService.createOrder(userUuid, createOrderDto);
  }

  @PostMapping("update/order")
  public ResponseEntity<Void> updateOrder(
      @RequestHeader(X_AUTH) UUID userUuid, @Valid @RequestBody UpdateOrderDto updateOrderDto) {
    return paymentService.updateOrder(userUuid, updateOrderDto);
  }
}

package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.*;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.UserSubscriptionDto;
import ru.notivent.service.SubscriptionService;

@RestController
@RequestMapping("subscription")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @GetMapping
  public ResponseEntity<UserSubscriptionDto> findUserSubscription(
      @RequestHeader(X_AUTH) UUID userUuid) {
    return subscriptionService.findUserSubscription(userUuid);
  }

  @GetMapping("check")
  public ResponseEntity<Boolean> isUserHasActiveSubscription(@RequestHeader(X_AUTH) UUID userId) {
    return ResponseEntity.ok(subscriptionService.isUserHasActiveSubscription(userId));
  }
}

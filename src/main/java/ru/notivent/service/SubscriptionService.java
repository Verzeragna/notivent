package ru.notivent.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.SubscriptionDao;
import ru.notivent.dto.TariffDto;
import ru.notivent.dto.UserSubscriptionDto;
import ru.notivent.mapper.TariffMapper;
import ru.notivent.model.Subscription;
import ru.notivent.model.Tariff;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final UserService userService;
  private final TariffService tariffService;
  private final TariffMapper tariffMapper;

  @Delegate private final SubscriptionDao subscriptionDao;

  public void subscribe(UUID userUuid, TariffDto dto) {
    val user = userService.findById(userUuid);
    if (user.isEmpty()) {
      log.error("User with UUID {} not found", userUuid);
    }
    var currentSubscription = findByUserUuid(userUuid);
    if (currentSubscription.isPresent()) {
      updateSubscription(currentSubscription.get());
    } else {
      createSubscription(userUuid, dto);
    }
  }

  private void updateSubscription(Subscription subscription) {
    val endAt = Instant.now().plus(1, ChronoUnit.YEARS);
    updateEndAt(endAt, subscription.getUuid());
  }

  private void createSubscription(UUID userUuid, TariffDto dto) {
    val endAt = Instant.now().plus(1, ChronoUnit.YEARS);
    var subscription =
        Subscription.builder()
            .tariff(Tariff.builder().uuid(dto.getUuid()).build())
            .userUuid(userUuid)
            .endAt(endAt)
            .build();
    create(subscription);
  }

  public ResponseEntity<UserSubscriptionDto> findUserSubscription(UUID userUuid) {
    val user = userService.findById(userUuid);
    if (user.isEmpty()) {
      log.error("User with UUID {} not found", userUuid);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var currentSubscription = findByUserUuid(userUuid);
    var tariff = tariffService.findAll();
    var subscriptionDto = new UserSubscriptionDto();
    subscriptionDto.setTariff(tariffMapper.toDto(tariff));
    if (currentSubscription.isPresent()) {
      var subscription = currentSubscription.get();
      subscriptionDto.setEndAt(subscription.getEndAt().toString());
    }
    return ResponseEntity.ok(subscriptionDto);
  }

  public boolean isUserHasActiveSubscription(UUID userUuid) {
    var currentSubscription = findByUserUuid(userUuid);
    if (currentSubscription.isEmpty()) return false;
    var subscription = currentSubscription.get();
    return subscription.getEndAt().isAfter(Instant.now());
  }
}

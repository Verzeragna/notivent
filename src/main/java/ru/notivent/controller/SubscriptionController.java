package ru.notivent.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.TariffDto;
import ru.notivent.dto.UserSubscriptionDto;
import ru.notivent.service.SubscriptionService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;
import static ru.notivent.util.HttpUtil.*;

@RestController
@RequestMapping("subscription")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("subscribe")
    public ResponseEntity<Void> subscribe(@RequestHeader(X_AUTH) UUID userUuid, @RequestBody TariffDto dto) {
        subscriptionService.subscribe(userUuid, dto);
        return ok().build();
    }

    @GetMapping
    public ResponseEntity<UserSubscriptionDto> findUserSubscription(@RequestHeader(X_AUTH) UUID userUuid) {
        return subscriptionService.findUserSubscription(userUuid);
    }
}

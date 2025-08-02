package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.X_AUTH;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.TariffDto;
import ru.notivent.service.TariffService;

@RestController
@RequestMapping("tariff")
@RequiredArgsConstructor
public class TariffController {

  private final TariffService tariffService;

  @GetMapping("all")
  public ResponseEntity<TariffDto> getAllTariffs(@RequestHeader(X_AUTH) UUID userUuid) {
    return tariffService.getAllTariffs(userUuid);
  }
}

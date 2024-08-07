package ru.notivent.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.TariffDao;
import ru.notivent.dto.TariffDto;
import ru.notivent.mapper.TariffMapper;
import ru.notivent.model.Tariff;

@Slf4j
@Service
@RequiredArgsConstructor
public class TariffService {

  @Delegate private final TariffDao tariffDao;

  private final UserService userService;
  private final TariffMapper tariffMapper;

  public ResponseEntity<TariffDto> getAllTariffs(UUID userUuid) {
    val user = userService.findById(userUuid);
    if (user.isEmpty()) {
      log.error("User with UUID {} not found", userUuid);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(tariffMapper.toDto(findAll()));
  }
}

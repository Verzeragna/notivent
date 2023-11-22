package ru.notivent.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.notivent.dao.TariffDao;
import ru.notivent.dto.TariffDto;
import ru.notivent.exception.UserNotFoundException;
import ru.notivent.mapper.TariffMapper;

@Service
@RequiredArgsConstructor
public class TariffService {

  @Delegate private final TariffDao tariffDao;

  private final UserService userService;
  private final TariffMapper tariffMapper;

  public TariffDto getAllTariffs(UUID userUuid) {
    val user = userService.findById(userUuid);
    if (user.isEmpty())
      throw new UserNotFoundException(
          "User with ID %s not found".formatted(userUuid), HttpStatus.BAD_REQUEST);
    return tariffMapper.toDto(findAll());
  }
}

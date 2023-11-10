package ru.notivent.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.notivent.dto.AuthDto;
import ru.notivent.exception.NotiventException;
import ru.notivent.model.User;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

  private final UserService userService;

  public UUID authRegister(AuthDto dto) {
    var user = userService.findByUserName(dto.getUserName());
    if (user.isEmpty()) {
        var newUser = createUser(dto);
        return newUser.getUuid();
    }
    if (comparePasswords(dto.getPassword(), user.get().getPassword())) {
        return user.get().getUuid();
    }
    throw new NotiventException(HttpStatus.BAD_REQUEST);
  }

  private User createUser(AuthDto dto) {
    var newUser = User.builder().userName(dto.getUserName()).password(dto.getPassword()).build();
    return userService.create(newUser);
  }

  private boolean comparePasswords(String dtoPassword, String dbPassword) {
      return Objects.equals(dtoPassword, dbPassword);
  }
}

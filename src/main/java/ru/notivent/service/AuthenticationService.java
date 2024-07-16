package ru.notivent.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dto.AuthDto;
import ru.notivent.model.User;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

  final UserService userService;
  final PasswordService passwordService;

  public ResponseEntity<UUID> authRegister(AuthDto dto) {
    var userOpt = userService.findByLogin(passwordService.decodeBase64(dto.getLogin()));
    if (userOpt.isEmpty()) {
      var newUser = createUser(dto);
      return ResponseEntity.ok(newUser.getUuid());
    }
    var user = userOpt.get();
    if (passwordService.matches(passwordService.decodeBase64(dto.getPassword()), user.getPassword())) {
      return ResponseEntity.ok(user.getUuid());
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  private User createUser(AuthDto dto) {
    dto.setLogin(passwordService.decodeBase64(dto.getLogin()));
    dto.setPassword(passwordService.encrypt(passwordService.decodeBase64(dto.getPassword())));
    var newUser = User.builder().login(dto.getLogin()).password(dto.getPassword()).build();
    return userService.create(newUser);
  }

}

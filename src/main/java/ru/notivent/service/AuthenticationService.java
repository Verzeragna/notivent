package ru.notivent.service;

import java.util.Objects;
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

  private final UserService userService;

  public ResponseEntity<UUID> authRegister(AuthDto dto) {
    var user = userService.findByUserName(dto.getUserName());
    if (user.isEmpty()) {
        var newUser = createUser(dto);
        return ResponseEntity.ok(newUser.getUuid());
    }
    if (comparePasswords(dto.getPassword(), user.get().getPassword())) {
        return ResponseEntity.ok(user.get().getUuid());
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  private User createUser(AuthDto dto) {
    var newUser = User.builder().userName(dto.getUserName()).password(dto.getPassword()).build();
    return userService.create(newUser);
  }

  private boolean comparePasswords(String dtoPassword, String dbPassword) {
      return Objects.equals(dtoPassword, dbPassword);
  }
}

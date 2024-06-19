package ru.notivent.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.UserDao;
import ru.notivent.dto.PasswordChangeDto;

@Service
@AllArgsConstructor
public class UserService {

  @Delegate final UserDao userDao;
  final EmailService emailService;
  final PasswordService passwordService;

  public String getUserName(UUID userUuid) {
    var user = userDao.findById(userUuid);
    if (user.isPresent()) {
      return user.get().getUserName();
    }
    return "";
  }

  public ResponseEntity<Void> setUserName(UUID userUuid, String userName) {
    var user = userDao.findById(userUuid);
    if (user.isPresent()) {
      var userWithCurrentUserName = userDao.findByUserName(userName);
      if (userWithCurrentUserName.isPresent() && !userWithCurrentUserName.get().getUuid().equals(user.get().getUuid())) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
      user.ifPresent(u -> userDao.updateUserNameById(userUuid, userName));
      return ResponseEntity.ok().build();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<Void> passwordReset(String email) {
    var userOpt = findByLogin(email);
    if (userOpt.isPresent()) {
      var user = userOpt.get();
      var newPassword = passwordService.generatePassword();
      var encryptedPassword = passwordService.encrypt(newPassword);
      updateUserPassword(user.getUuid(), encryptedPassword);
      emailService.sendEmail(email, newPassword);
      return ResponseEntity.ok().build();
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  public ResponseEntity<Void> passwordChange(UUID userUuid, PasswordChangeDto dto) {
    var userOpt = findById(userUuid);
    if (userOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    var user = userOpt.get();
    var oldPassword = passwordService.decodeBase64(dto.getOldPassword());
    if (passwordService.matches(oldPassword, user.getPassword())) {
      var encryptedPassword = passwordService.encrypt(oldPassword);
      updateUserPassword(user.getUuid(), encryptedPassword);
      return ResponseEntity.ok().build();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}

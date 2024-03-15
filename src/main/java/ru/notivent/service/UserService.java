package ru.notivent.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.UserDao;

@Service
@AllArgsConstructor
public class UserService {

  @Delegate final UserDao userDao;
  final EmailService emailService;
  final PasswordService passwordService;

  public String getNickName(UUID userUuid) {
    var user = userDao.findById(userUuid);
    if (user.isPresent()) {
      return user.get().getNickName();
    }
    return "";
  }

  public void setNickName(UUID userUuid, String userName) {
    var user = userDao.findById(userUuid);
    user.ifPresent(u -> userDao.updateNickNameById(userUuid, userName));
  }

  public ResponseEntity<Void> resetPassword(String email) {
    var userOpt = findByUserName(email);
    if (userOpt.isPresent()) {
      var user = userOpt.get();
      var newPassword = passwordService.generatePassword();
      var encryptedPassword = passwordService.encrypt(newPassword);
      updateUserPassword(user.getUuid(), encryptedPassword);
      emailService.sendEmail(email, newPassword);
      return ResponseEntity.ok().build();
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}

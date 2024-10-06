package ru.notivent.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.UserDao;
import ru.notivent.dto.PasswordChangeDto;
import ru.notivent.dto.UserProfileDto;
import ru.notivent.dto.UserProfileImageDto;
import ru.notivent.mapper.UserMapper;
import ru.notivent.model.User;
import ru.notivent.service.yandex.s3.ClientS3;
import ru.notivent.service.yandex.s3.S3Service;

@Service
@RequiredArgsConstructor
public class UserService {
  
  final UserDao userDao;
  final EmailService emailService;
  final PasswordService passwordService;
  final UserMapper userMapper;
  final S3Service s3Service;
  final ClientS3 clientS3;

  public Optional<User> findById(UUID uuid) {
    return userDao.findById(uuid);
  }

  public List<User> findByIds(Collection<UUID> ids) {
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    return userDao.findByIds(ids);
  }

  public Optional<User> findByUserName(String userName) {
    return userDao.findByUserName(userName.toLowerCase());
  }

  public Optional<User> findByLogin(String login) {
    return userDao.findByLogin(login);
  }

  public User create(User entity) {
    return userDao.create(entity);
  }

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
    var userOpt = userDao.findByLogin(email);
    if (userOpt.isPresent()) {
      var user = userOpt.get();
      var newPassword = passwordService.generatePassword();
      var encryptedPassword = passwordService.encrypt(newPassword);
      userDao.updateUserPassword(user.getUuid(), encryptedPassword);
      emailService.sendEmail(email, newPassword);
      return ResponseEntity.ok().build();
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  public ResponseEntity<Void> passwordChange(UUID userUuid, PasswordChangeDto dto) {
    var userOpt = userDao.findById(userUuid);
    if (userOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    var user = userOpt.get();
    var oldPassword = passwordService.decodeBase64(dto.getOldPassword());
    if (passwordService.matches(oldPassword, passwordService.decodeBase64(user.getPassword()))) {
      var encryptedPassword = passwordService.encrypt(oldPassword);
      userDao.updateUserPassword(user.getUuid(), encryptedPassword);
      return ResponseEntity.ok().build();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<UserProfileDto> getUserProfile(UUID userId) {
    var userOpt = userDao.findById(userId);
    if (userOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    var user = userOpt.get();
    return ResponseEntity.ok(userMapper.toProfileDto(user));
  }

  public ResponseEntity<Void> saveUserProfileImage(UserProfileImageDto dto) {
    var userOpt = userDao.findById(dto.userId());
    if (userOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    var profileImageUrl = s3Service.saveProfileImage(dto.image(), dto.userId(), clientS3);
    userDao.updateProfileImage(dto.userId(), profileImageUrl);
    return ResponseEntity.ok().build();
  }
}

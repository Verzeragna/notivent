package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.X_AUTH;

import java.util.UUID;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.PasswordChangeDto;
import ru.notivent.dto.PasswordResetDto;
import ru.notivent.dto.UserProfileDto;
import ru.notivent.dto.UserProfileImageDto;
import ru.notivent.service.UserService;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("name")
    public ResponseEntity<String> getUserName(@RequestHeader(X_AUTH) UUID userUuid) {
        return ResponseEntity.ok(userService.getUserName(userUuid));
    }

    @PostMapping("name")
    public ResponseEntity<Void> setUserName(@RequestHeader(X_AUTH) UUID userUuid, @RequestBody String userName) {
        return userService.setUserName(userUuid, userName);
    }

    @PostMapping("password/reset")
    public ResponseEntity<Void> passwordReset(@RequestBody PasswordResetDto dto) {
        return userService.passwordReset(dto.getLogin());
    }

    @PostMapping("password/change")
    public ResponseEntity<Void> passwordChange(@RequestHeader(X_AUTH) UUID userUuid, @Valid @RequestBody PasswordChangeDto dto) {
        return userService.passwordChange(userUuid, dto);
    }

    @GetMapping("profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestHeader(X_AUTH) UUID userUuid) {
        return userService.getUserProfile(userUuid);
    }

    @PostMapping("profile/image")
    public ResponseEntity<Void> saveUserProfileImage(@RequestBody UserProfileImageDto dto) {
        return userService.saveUserProfileImage(dto);
    }
}

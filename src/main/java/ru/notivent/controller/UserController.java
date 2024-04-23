package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.X_AUTH;

import java.util.UUID;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.PasswordChangeDto;
import ru.notivent.dto.PasswordResetDto;
import ru.notivent.service.UserService;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("nickName")
    public ResponseEntity<String> getNickName(@RequestHeader(X_AUTH) UUID userUuid) {
        return ResponseEntity.ok(userService.getNickName(userUuid));
    }

    @PostMapping("nickName")
    public ResponseEntity<Void> setNickName(@RequestHeader(X_AUTH) UUID userUuid, @RequestBody String userName) {
        return userService.setNickName(userUuid, userName);
    }

    @PostMapping("password/reset")
    public ResponseEntity<Void> passwordReset(@RequestBody PasswordResetDto dto) {
        return userService.passwordReset(dto.getEmail());
    }

    @PostMapping("password/change")
    public ResponseEntity<Void> passwordChange(@RequestHeader(X_AUTH) UUID userUuid, @Valid @RequestBody PasswordChangeDto dto) {
        return userService.passwordChange(userUuid, dto);
    }
}

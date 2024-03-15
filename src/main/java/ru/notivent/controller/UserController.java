package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.X_UUID;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.ResetPasswordDto;
import ru.notivent.service.UserService;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("nickName")
    public ResponseEntity<String> getNickName(@RequestHeader(X_UUID) UUID userUuid) {
        return ResponseEntity.ok(userService.getNickName(userUuid));
    }

    @PostMapping("nickName")
    public ResponseEntity<Void> setNickName(@RequestHeader(X_UUID) UUID userUuid, @RequestBody String userName) {
        userService.setNickName(userUuid, userName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset/password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDto dto) {
        return userService.resetPassword(dto.getEmail());
    }
}

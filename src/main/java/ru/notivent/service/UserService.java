package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;
import ru.notivent.dao.UserDao;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    @Delegate
    private final UserDao userDao;

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
}

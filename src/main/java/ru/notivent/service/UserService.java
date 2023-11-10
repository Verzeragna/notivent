package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;
import ru.notivent.dao.UserDao;

@Service
@AllArgsConstructor
public class UserService {

    @Delegate
    private final UserDao userDao;
}

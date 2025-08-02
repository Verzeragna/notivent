package ru.notivent.dao;

import org.springframework.stereotype.Repository;
import ru.notivent.model.Notification;

import java.util.Optional;

@Repository
public interface NotificationDao {
    Optional<Notification> findLastActive();
}

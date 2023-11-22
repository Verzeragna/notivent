package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao {

    User create(@Param("entity") User entity);

    Optional<User> findById(@Param("uuid") UUID uuid);

    Optional<User> findByUserName(@Param("userName") String userName);
}

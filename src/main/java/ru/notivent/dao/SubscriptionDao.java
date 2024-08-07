package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.Subscription;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionDao {

    Optional<Subscription> findByUserUuid(@Param("userUuid") UUID userUuid);

    void updateEndAt(@Param("endAt") OffsetDateTime endAt,
                     @Param("uuid") UUID subscriptionUuid);

    void create(@Param("entity") Subscription entity);
}

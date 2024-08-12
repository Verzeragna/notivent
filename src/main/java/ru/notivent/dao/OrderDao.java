package ru.notivent.dao;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.enums.OrderStatus;
import ru.notivent.model.Order;

@Repository
public interface OrderDao {

    Order create(@Param("entity") Order order);

    Optional<Order> findById(@Param("orderId") int orderId);

    void updateStatusById(@Param("orderId") int orderId, @Param("status") OrderStatus status, @Param("updatedAt") OffsetDateTime updatedAt);

    void updateSubscription(@Param("orderId") int orderId, @Param("subscriptionId") UUID subscriptionId);
}

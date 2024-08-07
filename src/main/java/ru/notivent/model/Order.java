package ru.notivent.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    Integer id;
    UUID userId;
    UUID subscriptionId;
    UUID tariffId;
    OrderStatus status;
    OffsetDateTime createdAt;
}

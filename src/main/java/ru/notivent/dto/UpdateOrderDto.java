package ru.notivent.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDto {
    int orderId;
    @NotNull
    UUID tariffId;
    @NotNull
    OrderStatus status;
    @NotNull
    Instant updatedAt;
}

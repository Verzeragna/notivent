package ru.notivent.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.TariffType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffDto {
    UUID uuid;
    String name;
    TariffType type;
    BigDecimal price;
}

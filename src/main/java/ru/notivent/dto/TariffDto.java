package ru.notivent.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.TariffType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TariffDto {
    @NotNull
    UUID uuid;
    String name;
    String subtitle;
    TariffType type;
    BigDecimal price;
}

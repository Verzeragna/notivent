package ru.notivent.mapper;

import org.mapstruct.Mapper;
import ru.notivent.dto.TariffDto;
import ru.notivent.model.Tariff;

@Mapper(componentModel = "spring")
public interface TariffMapper {
    Tariff toModel(TariffDto dto);
    TariffDto toDto(Tariff model);
}

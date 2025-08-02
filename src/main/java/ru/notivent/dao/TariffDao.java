package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.Tariff;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TariffDao {
    Tariff findAll();

    Optional<Tariff> findById(@Param("tariffId") UUID tariffId);
}

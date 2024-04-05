package ru.notivent.dao;

import org.springframework.stereotype.Repository;
import ru.notivent.model.Tariff;

import java.util.List;
import java.util.Optional;

@Repository
public interface TariffDao {
    Tariff findAll();

    Tariff findTariffWithPaymentParameters();
}

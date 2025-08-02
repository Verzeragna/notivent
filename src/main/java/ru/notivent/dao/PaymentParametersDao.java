package ru.notivent.dao;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.notivent.model.PaymentParameters;

@Repository
public interface PaymentParametersDao {
    
    Optional<PaymentParameters> find();
}

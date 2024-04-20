package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.Location;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationDao {

    UUID save(@Param("entity") Location location);

    Optional<Location> findByAddressLine(@Param("addressLine") String addressLine);
}

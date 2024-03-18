package ru.notivent.dao;

import org.springframework.stereotype.Repository;
import ru.notivent.model.Endpoint;

import java.util.List;

@Repository
public interface EndpointDao {
    List<Endpoint> findAll();
}

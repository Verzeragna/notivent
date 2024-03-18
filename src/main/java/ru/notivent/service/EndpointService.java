package ru.notivent.service;

import static java.util.stream.Collectors.toMap;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.EndpointDao;
import ru.notivent.enums.EndpointName;
import ru.notivent.model.Endpoint;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EndpointService {
  final EndpointDao endpointDao;

  public ResponseEntity<Map<EndpointName, String>> getAll() {
    var endpoints = endpointDao.findAll();
    if (endpoints.isEmpty()) {
      return ResponseEntity.ok(Collections.emptyMap());
    }
    return ResponseEntity.ok(endpoints.stream().collect(toMap(Endpoint::getName, Endpoint::getEndpoint)));
  }
}

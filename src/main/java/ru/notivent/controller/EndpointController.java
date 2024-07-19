package ru.notivent.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.enums.EndpointName;
import ru.notivent.service.EndpointService;

import java.util.Map;

@RestController
@RequestMapping("endpoint")
@RequiredArgsConstructor
public class EndpointController {
    final EndpointService endpointService;

    @GetMapping("get/all")
    public ResponseEntity<Map<EndpointName, String>> getAllEndpoints() {
        return endpointService.getAll();
    }

}

package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.EndpointName;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {
    UUID uuid;
    EndpointName name;
    String endpoint;
}

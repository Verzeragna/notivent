package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {
    UUID id;
    String postalCode;
    String country;
    String area;
    String city;
    String street;
    String houseNumber;
    String addressLine;
}

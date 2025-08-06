package ru.notivent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto implements Serializable {
    String postalCode;
    String country;
    String area;
    String city;
    String street;
    String houseNumber;
    String addressLine;
}

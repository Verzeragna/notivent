package ru.notivent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    String postalCode;
    String country;
    String area;
    String city;
    String street;
    String houseNumber;
    String addressLine;
}

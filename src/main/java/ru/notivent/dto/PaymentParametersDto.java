package ru.notivent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentParametersDto {
    UUID uuid;
    String terminalKey;
    String publicKey;
}

package co.wompi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentMethod(
        String type,

        @JsonProperty("phone_number")
        String phoneNumber
) {
}
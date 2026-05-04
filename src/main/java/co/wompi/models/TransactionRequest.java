package co.wompi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionRequest(

        @JsonProperty("acceptance_token")
        String acceptanceToken,

        @JsonProperty("accept_personal_auth")
        String acceptPersonalAuth,

        @JsonProperty("amount_in_cents")
        Integer amountInCents,

        String currency,

        String signature,

        @JsonProperty("customer_email")
        String customerEmail,

        String reference,

        @JsonProperty("payment_method")
        PaymentMethod paymentMethod
) {
}
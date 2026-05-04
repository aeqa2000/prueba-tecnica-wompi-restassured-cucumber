package co.wompi.services;

import co.wompi.clients.MerchantClient;
import co.wompi.clients.TransactionClient;
import co.wompi.context.ScenarioContext;
import co.wompi.models.PaymentMethod;
import co.wompi.models.TransactionRequest;
import co.wompi.utils.Config;
import co.wompi.utils.CryptoUtils;
import co.wompi.utils.Logs;
import io.restassured.response.Response;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TransactionService {

    private final TransactionClient transactionClient;
    private final MerchantClient merchantClient;
    private final ScenarioContext context;

    public TransactionService(
            TransactionClient transactionClient,
            MerchantClient merchantClient,
            ScenarioContext context
    ) {
        this.transactionClient = transactionClient;
        this.merchantClient = merchantClient;
        this.context = context;
    }

    public void generateUniqueReference() {
        String reference = "NEQUI-" + System.currentTimeMillis();
        context.setReference(reference);

        Logs.info("Referencia generada: " + reference);
    }

    public void generateIntegritySignature() {
        String rawSignature = context.getReference()
                + Config.getInt("amount.in.cents")
                + Config.get("currency")
                + Config.get("integrity.key");

        String signature = CryptoUtils.sha256(rawSignature);

        context.setSignature(signature);

        Logs.info("Firma de integridad generada correctamente");
    }

    public void createNequiTransaction(String phoneNumber) {
        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                context.getSignature(),
                phoneNumber,
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
        saveTransactionIdIfCreated();
    }

    public void createNequiTransactionWithoutAcceptanceToken() {
        TransactionRequest request = buildNequiTransactionRequest(
                null,
                context.getAcceptPersonalAuth(),
                context.getSignature(),
                "3991111111",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithoutAcceptPersonalAuth() {
        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                null,
                context.getSignature(),
                "3991111111",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
        saveTransactionIdIfCreated();
    }

    public void createNequiTransactionWithInvalidSignature() {
        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                "firma-invalida-manual",
                "3991111111",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithoutPaymentMethod() {
        TransactionRequest request = new TransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                Config.getInt("amount.in.cents"),
                Config.get("currency"),
                context.getSignature(),
                Config.get("customer.email"),
                context.getReference(),
                null
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithInvalidPaymentMethodType() {
        TransactionRequest request = new TransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                Config.getInt("amount.in.cents"),
                Config.get("currency"),
                context.getSignature(),
                Config.get("customer.email"),
                context.getReference(),
                new PaymentMethod("NEQUIX", "3991111111")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithInvalidPhoneNumber() {
        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                context.getSignature(),
                "123",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithInvalidAmount() {
        int invalidAmount = 0;

        String invalidSignature = CryptoUtils.sha256(
                context.getReference()
                        + invalidAmount
                        + Config.get("currency")
                        + Config.get("integrity.key")
        );

        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                invalidSignature,
                "3991111111",
                invalidAmount,
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithInvalidCurrency() {
        String invalidCurrency = "USD";

        String invalidSignature = CryptoUtils.sha256(
                context.getReference()
                        + Config.getInt("amount.in.cents")
                        + invalidCurrency
                        + Config.get("integrity.key")
        );

        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                invalidSignature,
                "3991111111",
                Config.getInt("amount.in.cents"),
                invalidCurrency
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createAnotherNequiTransactionWithSameReference() {
        refreshAcceptanceTokens();

        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                context.getSignature(),
                "3991111111",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransaction(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithInvalidPrivateKey() {
        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                context.getSignature(),
                "3991111111",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransactionWithInvalidPrivateKey(request);

        context.setLastResponse(response);
    }

    public void createNequiTransactionWithoutAuthorization() {
        TransactionRequest request = buildNequiTransactionRequest(
                context.getAcceptanceToken(),
                context.getAcceptPersonalAuth(),
                context.getSignature(),
                "3991111111",
                Config.getInt("amount.in.cents"),
                Config.get("currency")
        );

        Response response = transactionClient.createTransactionWithoutAuthorization(request);

        context.setLastResponse(response);
    }

    public void getNonExistentTransaction() {
        Response response = transactionClient.getTransaction("trx_inexistente_123456789");

        context.setLastResponse(response);
    }

    public void validateTransactionWasCreatedSuccessfully() {
        context.getLastResponse()
                .then()
                .statusCode(201)
                .body("data.id", notNullValue())
                .body("data.reference", equalTo(context.getReference()));

        String transactionId = context.getLastResponse()
                .jsonPath()
                .getString("data.id");

        context.setTransactionId(transactionId);

        Logs.info("Transacción creada exitosamente con id: " + transactionId);
    }

    public void validateFinalTransactionStatus(String expectedStatus) throws InterruptedException {
        String currentStatus = getFinalTransactionStatus();

        assertThat(currentStatus, equalTo(expectedStatus));
    }

    public void validateDocumentedFinalTransactionStatus(String documentedExpectedStatus) throws InterruptedException {
        String currentStatus = getFinalTransactionStatus();

        if (!documentedExpectedStatus.equals(currentStatus)) {
            Logs.warning("Hallazgo: la documentación indica estado final "
                    + documentedExpectedStatus
                    + ", pero el ambiente respondió "
                    + currentStatus);
        }

        assertThat(currentStatus, equalTo(documentedExpectedStatus));
    }

    public void validateStatusCode(int expectedStatusCode) {
        context.getLastResponse()
                .then()
                .statusCode(expectedStatusCode);
    }

    private TransactionRequest buildNequiTransactionRequest(
            String acceptanceToken,
            String acceptPersonalAuth,
            String requestSignature,
            String phoneNumber,
            Integer amountInCents,
            String currency
    ) {
        return new TransactionRequest(
                acceptanceToken,
                acceptPersonalAuth,
                amountInCents,
                currency,
                requestSignature,
                Config.get("customer.email"),
                context.getReference(),
                new PaymentMethod("NEQUI", phoneNumber)
        );
    }

    private String getFinalTransactionStatus() throws InterruptedException {
        String currentStatus = null;

        for (int attempt = 1; attempt <= 12; attempt++) {
            Response response = transactionClient.getTransaction(context.getTransactionId());

            response.then().statusCode(200);

            currentStatus = response.jsonPath().getString("data.status");

            if (List.of("APPROVED", "DECLINED", "ERROR", "VOIDED").contains(currentStatus)) {
                break;
            }

            Thread.sleep(5000);
        }

        return currentStatus;
    }

    private void saveTransactionIdIfCreated() {
        if (context.getLastResponse().statusCode() == 201) {
            String transactionId = context.getLastResponse()
                    .jsonPath()
                    .getString("data.id");

            context.setTransactionId(transactionId);
        }
    }

    private void refreshAcceptanceTokens() {
        Response response = merchantClient.getMerchant();

        response.then().statusCode(200);

        String newAcceptanceToken = response.jsonPath()
                .getString("data.presigned_acceptance.acceptance_token");

        String newAcceptPersonalAuth = response.jsonPath()
                .getString("data.presigned_personal_data_auth.acceptance_token");

        assertThat(newAcceptanceToken, notNullValue());
        assertThat(newAcceptPersonalAuth, notNullValue());

        context.setAcceptanceToken(newAcceptanceToken);
        context.setAcceptPersonalAuth(newAcceptPersonalAuth);

        Logs.info("Tokens renovados para prueba de referencia duplicada");
    }
}
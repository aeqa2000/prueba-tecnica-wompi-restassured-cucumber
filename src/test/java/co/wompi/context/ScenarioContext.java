package co.wompi.context;

import io.restassured.response.Response;

public class ScenarioContext {

    private String acceptanceToken;
    private String acceptPersonalAuth;
    private String reference;
    private String signature;
    private String transactionId;
    private Response lastResponse;

    public String getAcceptanceToken() {
        return acceptanceToken;
    }

    public void setAcceptanceToken(String acceptanceToken) {
        this.acceptanceToken = acceptanceToken;
    }

    public String getAcceptPersonalAuth() {
        return acceptPersonalAuth;
    }

    public void setAcceptPersonalAuth(String acceptPersonalAuth) {
        this.acceptPersonalAuth = acceptPersonalAuth;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }
}
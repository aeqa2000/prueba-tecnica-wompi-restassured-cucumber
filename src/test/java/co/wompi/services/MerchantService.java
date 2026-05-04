package co.wompi.services;

import co.wompi.clients.MerchantClient;
import co.wompi.context.ScenarioContext;
import co.wompi.utils.Logs;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class MerchantService {

    private final MerchantClient merchantClient;
    private final ScenarioContext context;

    public MerchantService(MerchantClient merchantClient, ScenarioContext context) {
        this.merchantClient = merchantClient;
        this.context = context;
    }

    public void getAcceptanceTokens() {
        Response response = merchantClient.getMerchant();

        response.then().statusCode(200);

        String acceptanceToken = response.jsonPath()
                .getString("data.presigned_acceptance.acceptance_token");

        String acceptPersonalAuth = response.jsonPath()
                .getString("data.presigned_personal_data_auth.acceptance_token");

        assertThat(acceptanceToken, notNullValue());
        assertThat(acceptPersonalAuth, notNullValue());

        context.setAcceptanceToken(acceptanceToken);
        context.setAcceptPersonalAuth(acceptPersonalAuth);

        Logs.info("Tokens de aceptación obtenidos correctamente");
    }
}
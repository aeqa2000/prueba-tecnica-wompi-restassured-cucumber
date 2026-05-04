package co.wompi.clients;

import co.wompi.utils.Config;
import co.wompi.utils.RequestFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MerchantClient {

    public Response getMerchant() {
        return given()
                .filters(RequestFilter.logRequestAndResponse())
                .baseUri(Config.get("base.url"))
                .contentType(ContentType.JSON)
                .pathParam("publicKey", Config.get("public.key"))
                .when()
                .get("/merchants/{publicKey}");
    }
}
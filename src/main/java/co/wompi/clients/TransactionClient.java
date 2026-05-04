package co.wompi.clients;

import co.wompi.models.TransactionRequest;
import co.wompi.utils.Config;
import co.wompi.utils.RequestFilter;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TransactionClient {

    public Response createTransaction(TransactionRequest request) {
        return given()
                .filters(RequestFilter.logRequestAndResponse())
                .baseUri(Config.get("base.url"))
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + Config.get("private.key"))
                .body(request, ObjectMapperType.JACKSON_2)
                .when()
                .post("/transactions");
    }

    public Response createTransactionWithInvalidPrivateKey(TransactionRequest request) {
        return given()
                .filters(RequestFilter.logRequestAndResponse())
                .baseUri(Config.get("base.url"))
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer private_key_invalida")
                .body(request, ObjectMapperType.JACKSON_2)
                .when()
                .post("/transactions");
    }

    public Response createTransactionWithoutAuthorization(TransactionRequest request) {
        return given()
                .filters(RequestFilter.logRequestAndResponse())
                .baseUri(Config.get("base.url"))
                .contentType(ContentType.JSON)
                .body(request, ObjectMapperType.JACKSON_2)
                .when()
                .post("/transactions");
    }

    public Response getTransaction(String transactionId) {
        return given()
                .filters(RequestFilter.logRequestAndResponse())
                .baseUri(Config.get("base.url"))
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + Config.get("public.key"))
                .pathParam("transactionId", transactionId)
                .when()
                .get("/transactions/{transactionId}");
    }
}
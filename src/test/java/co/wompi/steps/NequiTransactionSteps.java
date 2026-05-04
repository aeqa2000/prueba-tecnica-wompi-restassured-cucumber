package co.wompi.steps;

import co.wompi.clients.MerchantClient;
import co.wompi.clients.TransactionClient;
import co.wompi.context.ScenarioContext;
import co.wompi.services.MerchantService;
import co.wompi.services.TransactionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class NequiTransactionSteps {

    private final ScenarioContext context = new ScenarioContext();

    private final MerchantClient merchantClient = new MerchantClient();
    private final TransactionClient transactionClient = new TransactionClient();

    private final MerchantService merchantService = new MerchantService(
            merchantClient,
            context
    );

    private final TransactionService transactionService = new TransactionService(
            transactionClient,
            merchantClient,
            context
    );

    @Given("obtengo los tokens de aceptación del comercio")
    public void obtengoLosTokensDeAceptacionDelComercio() {
        merchantService.getAcceptanceTokens();
    }

    @Given("genero una referencia única para la transacción")
    public void generoUnaReferenciaUnicaParaLaTransaccion() {
        transactionService.generateUniqueReference();
    }

    @Given("genero la firma de integridad")
    public void generoLaFirmaDeIntegridad() {
        transactionService.generateIntegritySignature();
    }

    @When("creo una transacción Nequi con el número {string}")
    public void creoUnaTransaccionNequiConElNumero(String phoneNumber) {
        transactionService.createNequiTransaction(phoneNumber);
    }

    @When("intento crear una transacción Nequi sin acceptance_token")
    public void intentoCrearUnaTransaccionNequiSinAcceptanceToken() {
        transactionService.createNequiTransactionWithoutAcceptanceToken();
    }

    @When("intento crear una transacción Nequi sin accept_personal_auth")
    public void intentoCrearUnaTransaccionNequiSinAcceptPersonalAuth() {
        transactionService.createNequiTransactionWithoutAcceptPersonalAuth();
    }

    @When("intento crear una transacción Nequi con firma inválida")
    public void intentoCrearUnaTransaccionNequiConFirmaInvalida() {
        transactionService.createNequiTransactionWithInvalidSignature();
    }

    @When("intento crear una transacción Nequi sin payment_method")
    public void intentoCrearUnaTransaccionNequiSinPaymentMethod() {
        transactionService.createNequiTransactionWithoutPaymentMethod();
    }

    @When("intento crear una transacción Nequi con tipo de método de pago incorrecto")
    public void intentoCrearUnaTransaccionNequiConTipoDeMetodoDePagoIncorrecto() {
        transactionService.createNequiTransactionWithInvalidPaymentMethodType();
    }

    @When("intento crear una transacción Nequi con número de celular inválido")
    public void intentoCrearUnaTransaccionNequiConNumeroDeCelularInvalido() {
        transactionService.createNequiTransactionWithInvalidPhoneNumber();
    }

    @When("intento crear una transacción Nequi con monto inválido")
    public void intentoCrearUnaTransaccionNequiConMontoInvalido() {
        transactionService.createNequiTransactionWithInvalidAmount();
    }

    @When("intento crear una transacción Nequi con moneda inválida")
    public void intentoCrearUnaTransaccionNequiConMonedaInvalida() {
        transactionService.createNequiTransactionWithInvalidCurrency();
    }

    @When("intento crear otra transacción Nequi con la misma referencia")
    public void intentoCrearOtraTransaccionNequiConLaMismaReferencia() {
        transactionService.createAnotherNequiTransactionWithSameReference();
    }

    @When("intento crear una transacción Nequi con private key inválida")
    public void intentoCrearUnaTransaccionNequiConPrivateKeyInvalida() {
        transactionService.createNequiTransactionWithInvalidPrivateKey();
    }

    @When("intento crear una transacción Nequi sin Authorization")
    public void intentoCrearUnaTransaccionNequiSinAuthorization() {
        transactionService.createNequiTransactionWithoutAuthorization();
    }

    @When("consulto una transacción inexistente")
    public void consultoUnaTransaccionInexistente() {
        transactionService.getNonExistentTransaction();
    }

    @Then("la transacción debe crearse exitosamente")
    public void laTransaccionDebeCrearseExitosamente() {
        transactionService.validateTransactionWasCreatedSuccessfully();
    }

    @Then("el estado final de la transacción debe ser {string}")
    public void elEstadoFinalDeLaTransaccionDebeSer(String expectedStatus) throws InterruptedException {
        transactionService.validateFinalTransactionStatus(expectedStatus);
    }

    @Then("el estado final documentado de la transacción debería ser {string}")
    public void elEstadoFinalDocumentadoDeLaTransaccionDeberiaSer(String documentedExpectedStatus) throws InterruptedException {
        transactionService.validateDocumentedFinalTransactionStatus(documentedExpectedStatus);
    }

    @Then("la respuesta debe tener código de estado {int}")
    public void laRespuestaDebeTenerCodigoDeEstado(int expectedStatusCode) {
        transactionService.validateStatusCode(expectedStatusCode);
    }
}
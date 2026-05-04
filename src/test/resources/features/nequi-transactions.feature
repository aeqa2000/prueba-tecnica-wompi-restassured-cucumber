Feature: Transacciones Wompi con Nequi

  Background:
    Given obtengo los tokens de aceptación del comercio
    And genero una referencia única para la transacción
    And genero la firma de integridad

  @happyPath @nequi
  Scenario Outline: Crear transacción Nequi y validar estado final
    When creo una transacción Nequi con el número "<phoneNumber>"
    Then la transacción debe crearse exitosamente
    And el estado final de la transacción debe ser "<expectedStatus>"

    Examples:
      | phoneNumber | expectedStatus |
      | 3991111111  | APPROVED       |
      | 3992222222  | DECLINED       |

  @knownIssue @nequi
  Scenario: Crear transacción Nequi con número no parametrizado y validar comportamiento documentado
    When creo una transacción Nequi con el número "3993333333"
    Then la transacción debe crearse exitosamente
    And el estado final documentado de la transacción debería ser "ERROR"

  @knownIssue @validation
  Scenario: Intentar crear transacción Nequi sin accept_personal_auth
    When intento crear una transacción Nequi sin accept_personal_auth
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción sin acceptance_token
    When intento crear una transacción Nequi sin acceptance_token
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción con firma inválida
    When intento crear una transacción Nequi con firma inválida
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción sin payment_method
    When intento crear una transacción Nequi sin payment_method
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción con payment_method.type incorrecto
    When intento crear una transacción Nequi con tipo de método de pago incorrecto
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción con phone_number inválido
    When intento crear una transacción Nequi con número de celular inválido
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción con amount_in_cents inválido
    When intento crear una transacción Nequi con monto inválido
    Then la respuesta debe tener código de estado 422

  @negative @validation
  Scenario: Intentar crear transacción con currency inválida
    When intento crear una transacción Nequi con moneda inválida
    Then la respuesta debe tener código de estado 422

  @negative @duplicate
  Scenario: Intentar crear una segunda transacción con la misma referencia
    When creo una transacción Nequi con el número "3991111111"
    Then la transacción debe crearse exitosamente
    When intento crear otra transacción Nequi con la misma referencia
    Then la respuesta debe tener código de estado 422

  @security @negative
  Scenario: Intentar crear transacción con private key inválida
    When intento crear una transacción Nequi con private key inválida
    Then la respuesta debe tener código de estado 401

  @security @negative
  Scenario: Intentar crear transacción sin Authorization
    When intento crear una transacción Nequi sin Authorization
    Then la respuesta debe tener código de estado 401

  @query @negative
  Scenario: Consultar una transacción inexistente
    When consulto una transacción inexistente
    Then la respuesta debe tener código de estado 404
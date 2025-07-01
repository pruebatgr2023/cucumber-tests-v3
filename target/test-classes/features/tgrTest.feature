Feature: Ingreso a TGR

  Background: Usuario abre TGR
    Given que se navega a la página "http://intranettest/SistemaGestionNewWeb/index"
    When en el elemento "#intra_username" se ingresa el texto "ralvarez"
    When en el elemento "#textfield2" se ingresa el texto "12345678"
    When se cliquea el elemento con id "btn1"
    And se espera que la página cargue completamente
    When se cliquea el elemento "autenticacion_req" con el texto "Sistema de Clientes"
    And se cambia a la pestaña número 2
    And se espera que la página cargue completamente
    Then la página debería contener el texto "Bienvenid@ al Sistema de Clientes"

  Scenario: el usuario entra SDC
    Given que se cliquea el selector "#estructura > div:nth-child(1) > div:nth-child(1) > a > h5"
    When en el elemento "#sc_rut" se ingresa el texto "18170016"
    When se cliquea el elemento con id "sc_buscar"
    And se espera 5 segundos
    Then la página debería contener el texto "Información Estado Civil"
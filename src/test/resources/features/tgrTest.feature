Feature: Ingreso a TGR

  Background: Usuario abre TGR
    Given que se navega a la pagina "url_intranet"
    When en el elemento "#intra_username" se ingresa el texto "usuario_intranet1"
    When en el elemento "#textfield2" se ingresa el texto "clave_intranet1"
    When se cliquea el elemento con id "btn1"
    And se espera que la pagina cargue completamente
    When se cliquea el elemento "autenticacion_req" con el texto "Sistema de Clientes"
    And se cambia a la pestaña numero 2
    And se espera que la pagina cargue completamente
    Then la pagina deberia contener el texto "Bienvenid@ al Sistema de Clientes"

  Scenario: el usuario entra SDC
    Given que se cliquea el selector "#estructura > div:nth-child(1) > div:nth-child(1) > a > h5"
    When en el elemento "#sc_rut" se ingresa el texto "18170016"
    When se cliquea el elemento con id "sc_buscar"
    And se espera 5 segundos
    Then la pagina deberia contener el texto "Información Estado Civil"
    When se hace foco en el elemento con xpath "//*[@id='divCorreos']/div[1]"
    And se espera 3 segundos
    When se cliquea el elemento con id "btnListaCorreo"
    Then la pagina deberia mostrar en "panel-heading" el texto "Listado de Correos Electrónicos"
    And se espera 3 segundos
package stepdefs;

import static utils.Hooks.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.ConfigReader;
//import io.cucumber.messages.types.Duration;
import utils.ExtentReportManager;
import utils.Hooks;

public class SimpleSteps {

@Given("que se navega a la pagina {string}")
public void navegar_a(String urlKey) {
    String url = ConfigReader.get(urlKey); // Lee desde el archivo .properties
    Hooks.driver.get(url);
    ExtentReportManager.logStep(Hooks.driver, "Navego a: " + url);
}

    @When("se cliquea el elemento con xpath {string}")
    @Given("que se cliquea el xpath {string}")
    public void click_por_xpath(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
        ExtentReportManager.logStep(driver, "Click en xpath: " + xpath);
    }

    @When("se cliquea el elemento con id {string}")
    @Given("que se cliquea el elemento con id {string}")
    public void click_por_id(String id) {
        WebElement element = driver.findElement(By.id(id));
        element.click();
        ExtentReportManager.logStep(driver, "Click en id: " + id);
    }


    @When("se cliquea el selector CSS {string}")
    @Given("que se cliquea el selector {string}")
    public void click_por_css(String selector) {
        WebElement element = driver.findElement(By.cssSelector(selector));
        element.click();
        ExtentReportManager.logStep(driver, "Click en selector: " + selector);
    }

    @When("se cliquea el elemento {string} con el texto {string}")
    @Given("que se cliquea el elemento {string} con el texto {string}")
    public void click_por_class_y_texto(String clase, String texto) {
    String xpath = "//*[contains(@class, '" + clase + "') and contains(text(),'" + texto + "')]";
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
        ExtentReportManager.logStep(driver, "Click en elemento con clase: " + clase + " y texto: " + texto);
    }


    @When("cliquea el elemento {string} con la clase {string}")
    @Given("se cliquea el elemento {string} con la clase {string}")
    public void click_por_clase(String tag, String clase) {
        WebElement element = driver.findElement(By.cssSelector(tag + "." + clase));
        element.click();
        ExtentReportManager.logStep(driver, "Click en " + tag + " con clase: " + clase);
    }

    @When("se cliquea el elemento {string} con la id {string}")
    @Given("que se cliquea el elemento {string} con la id {string}")
    public void click_por_id(String tag, String id) {
        WebElement element = driver.findElement(By.cssSelector(tag + "#" + id));
        element.click();
        ExtentReportManager.logStep(driver, "Click en " + tag + " con id: " + id);
    }

    @When("en el elemento {string} con id {string} se ingresa el texto {string}")
    @Given("que en el elemento {string} con id {string} se ingreso el texto {string}")
    public void escribir_por_id(String tag, String id, String texto) {
        WebElement element = driver.findElement(By.cssSelector(tag + "#" + id));
        element.clear();
        element.sendKeys(texto);
        ExtentReportManager.logStep(driver, "Texto ingresado en " + tag + "#" + id + ": " + texto);
    }

    @When("en el elemento {string} se ingresa el texto {string}")
    @Given("que en el elemento {string} se ingresó el texto {string}")
    public void escribir_por_selector(String selector, String texto) {
    try {
        String valor = ConfigReader.get(texto);
        if (valor != null && !valor.isEmpty()) {
            texto = valor;  // reemplaza si existe en properties texto exacto
        }
    } catch (NoSuchElementException | NullPointerException e) {
        // No se encontró la clave en el properties o es nulo, usar texto original
    } catch (Exception e) {
        // Cualquier otra excepción, opcionalmente loguear y continuar
    }

    WebElement element = driver.findElement(By.cssSelector(selector));
    element.clear();
    element.sendKeys(texto);
    ExtentReportManager.logStep(driver, "Texto ingresado en selector: " + selector + ": " + texto);
}

@When("se hace foco en el elemento con xpath {string}")
@Given("que se hace foco en el elemento con xpath {string}")
public void hacer_foco_por_xpath(String xpath) {
    WebElement element = driver.findElement(By.xpath(xpath));

    // 👇 Solo hacer foco al elemento (sin clic)
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

    ExtentReportManager.logStep(driver, "Se hizo foco en el elemento con xpath: " + xpath);
}

@Then("la pagina deberia mostrar en {string} el texto {string}")
public void validar_texto_en_clase(String clase, String textoEsperado) {
    // XPath que busca cualquier elemento con esa clase y texto contenido
    String xpath = "//*[contains(@class, '" + clase + "') and contains(text(), '" + textoEsperado + "')]";

    // Buscar el elemento
    WebElement elemento = driver.findElement(By.xpath(xpath));

    // Hacer scroll (foco) al elemento
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);

    // Validar que el texto está efectivamente ahí
    String textoReal = elemento.getText();
    assert textoReal.contains(textoEsperado) : "Texto no encontrado en clase '" + clase + "'. Esperado: " + textoEsperado;

    // Registrar paso en el reporte
    ExtentReportManager.logStep(driver, "Validación de texto: '" + textoEsperado + "' en clase: " + clase);
}

  @Then("la pagina deberia contener el texto {string}")
public void validar_texto_en_pagina(String textoEsperado) {
    // Buscar el elemento que contiene el texto (de forma parcial)
    WebElement elemento = driver.findElement(By.xpath("//*[contains(text(),'" + textoEsperado + "')]"));

    // 👇 Hacer scroll hasta el elemento
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);

    // Validar que el texto está en el body completo (opcional si ya lo encontraste arriba)
    String bodyText = driver.findElement(By.tagName("body")).getText();
    assert bodyText.contains(textoEsperado) : "Texto no encontrado en la pagina: " + textoEsperado;

    // Registrar paso en el reporte
    ExtentReportManager.logStep(driver, "Validación de texto visible: " + textoEsperado);
}

    @Then("se encuentra un elemento {string} con la id {string} con el texto {string}")
    public void validar_elemento_por_id_con_texto(String tag, String id, String texto) {
        String actual = driver.findElement(By.cssSelector(tag + "#" + id)).getText();
        assert actual.equals(texto);
        ExtentReportManager.logStep(driver, "Validacion de texto en " + tag + "#" + id + ": " + texto);
    }

    @Then("en el elemento {string} con la id {string} con el atributo {string} tiene el valor {string}")
    public void validar_atributo_valor(String tag, String id, String atributo, String valor) {
        String actual = driver.findElement(By.cssSelector(tag + "#" + id)).getAttribute(atributo);
        assert actual.equals(valor);
        ExtentReportManager.logStep(driver, "Validacion de atributo " + atributo + " con valor " + valor);
    }

@And("se espera {int} segundos")
public void esperar(int segundos) throws InterruptedException {
    long milisegundos = segundos * 1000L;
    Thread.sleep(milisegundos);
    ExtentReportManager.logStep(null, "⏳ Espero " + segundos + " segundos");
}

@And("se espera que la pagina cargue completamente")
public void esperarCargaCompletaPagina() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    try {
        wait.until(webDriver ->
            ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete")
        );
        ExtentReportManager.logStep(driver, "La pagina cargo completamente");
    } catch (TimeoutException e) {
        ExtentReportManager.test.fail("La página no termino de cargar tras 15 segundos");
        throw e;
    }
}

    @And("se cambia a la pestaña numero {int}")
    public void cambiar_a_pestana(int index) {
    // index parte en 1 para el usuario, pero en Java es 0-based
    int targetIndex = index - 1;

    // Obtener todas las pestañas
    List<String> pestañas = new ArrayList<>(Hooks.driver.getWindowHandles());

    // Verificar que el índice existe
    if (targetIndex < pestañas.size()) {
        Hooks.driver.switchTo().window(pestañas.get(targetIndex));
        ExtentReportManager.logStep(Hooks.driver, "Se cambio a la pestaña numero: " + index);
    } else {
        throw new IllegalArgumentException("No existe la pestaña numero: " + index);
    }
}
}
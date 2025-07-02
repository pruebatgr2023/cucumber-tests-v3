package stepdefs;

import static utils.Hooks.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.Hooks; 

public class SimpleSteps {

    @Given("que se navega a la pagina {string}")
    public void navegar_a(String urlKey) {
        String url = ConfigReader.get(urlKey);
        Hooks.driver.get(url);
        ExtentReportManager.logStepWithScreenshot(Hooks.driver, "Navego a: " + url);
    }

    @When("se cliquea el elemento con xpath {string}")
    @Given("que se cliquea el xpath {string}")
    public void click_por_xpath(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
        ExtentReportManager.logStepWithScreenshot(driver, "Click en xpath: " + xpath);
    }

    @When("se cliquea el elemento con id {string}")
    @Given("que se cliquea el elemento con id {string}")
    public void click_por_id(String id) {
        WebElement element = driver.findElement(By.id(id));
        element.click();
        ExtentReportManager.logStepWithScreenshot(driver, "Click en id: " + id);
    }

    @When("se cliquea el selector CSS {string}")
    @Given("que se cliquea el selector {string}")
    public void click_por_css(String selector) {
        WebElement element = driver.findElement(By.cssSelector(selector));
        element.click();
        ExtentReportManager.logStepWithScreenshot(driver, "Click en selector: " + selector);
    }

    @When("se cliquea el elemento {string} con el texto {string}")
    @Given("que se cliquea el elemento {string} con el texto {string}")
    public void click_por_class_y_texto(String clase, String texto) {
        String xpath = "//*[contains(@class, '" + clase + "') and contains(text(),'" + texto + "')]";
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
        ExtentReportManager.logStepWithScreenshot(driver, "Click en elemento con clase: " + clase + " y texto: " + texto);
    }

    @When("cliquea el elemento {string} con la clase {string}")
    @Given("se cliquea el elemento {string} con la clase {string}")
    public void click_por_clase(String tag, String clase) {
        WebElement element = driver.findElement(By.cssSelector(tag + "." + clase));
        element.click();
        ExtentReportManager.logStepWithScreenshot(driver, "Click en " + tag + " con clase: " + clase);
    }

    @When("se cliquea el elemento {string} con la id {string}")
    @Given("que se cliquea el elemento {string} con la id {string}")
    public void click_por_id(String tag, String id) {
        WebElement element = driver.findElement(By.cssSelector(tag + "#" + id));
        element.click();
        ExtentReportManager.logStepWithScreenshot(driver, "Click en " + tag + " con id: " + id);
    }

    @When("en el elemento {string} con id {string} se ingresa el texto {string}")
    @Given("que en el elemento {string} con id {string} se ingreso el texto {string}")
    public void escribir_por_id(String tag, String id, String texto) {
        WebElement element = driver.findElement(By.cssSelector(tag + "#" + id));
        element.clear();
        element.sendKeys(texto);
        ExtentReportManager.logStepWithScreenshot(driver, "Texto ingresado en " + tag + "#" + id + ": " + texto);
    }

    @When("en el elemento {string} se ingresa el texto {string}")
    @Given("que en el elemento {string} se ingresó el texto {string}")
    public void escribir_por_selector(String selector, String texto) {
        try {
            String valor = ConfigReader.get(texto);
            if (valor != null && !valor.isEmpty()) {
                texto = valor;
            }
        } catch (NoSuchElementException | NullPointerException e) {
            // No se encontró la clave en el properties o es nulo, usar texto original
        } catch (Exception e) {
            // Cualquier otra excepción, opcionalmente loguear y continuar
            // Hooks.scenarioTest.warning("Advertencia al leer de ConfigReader: " + e.getMessage()); // Opcional
        }

        WebElement element = driver.findElement(By.cssSelector(selector));
        element.clear();
        element.sendKeys(texto);
        ExtentReportManager.logStepWithScreenshot(driver, "Texto ingresado en selector: " + selector + ": " + texto);
    }

    @When("se hace foco en el elemento con xpath {string}")
    @Given("que se hace foco en el elemento con xpath {string}")
    public void hacer_foco_por_xpath(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        ExtentReportManager.logStepWithScreenshot(driver, "Se hizo foco en el elemento con xpath: " + xpath);
    }

    @Then("la pagina deberia mostrar en {string} el texto {string}")
    public void validar_texto_en_clase(String clase, String textoEsperado) {
        String xpath = "//*[contains(@class, '" + clase + "') and contains(text(), '" + textoEsperado + "')]";
        WebElement elemento = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
        String textoReal = elemento.getText();
        assert textoReal.contains(textoEsperado) : "Texto no encontrado en clase '" + clase + "'. Esperado: " + textoEsperado;
        ExtentReportManager.logStepWithScreenshot(driver, "Validación de texto: '" + textoEsperado + "' en clase: " + clase);
    }

    @Then("la pagina deberia contener el texto {string}")
    public void validar_texto_en_pagina(String textoEsperado) {
        WebElement elemento = driver.findElement(By.xpath("//*[contains(text(),'" + textoEsperado + "')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assert bodyText.contains(textoEsperado) : "Texto no encontrado en la pagina: " + textoEsperado;
        ExtentReportManager.logStepWithScreenshot(driver, "Validación de texto visible: " + textoEsperado);
    }

    @Then("se encuentra un elemento {string} con la id {string} con el texto {string}")
    public void validar_elemento_por_id_con_texto(String tag, String id, String texto) {
        String actual = driver.findElement(By.cssSelector(tag + "#" + id)).getText();
        assert actual.equals(texto);
        ExtentReportManager.logStepWithScreenshot(driver, "Validacion de texto en " + tag + "#" + id + ": " + texto);
    }

    @Then("en el elemento {string} con la id {string} con el atributo {string} tiene el valor {string}")
    public void validar_atributo_valor(String tag, String id, String atributo, String valor) {
        String actual = driver.findElement(By.cssSelector(tag + "#" + id)).getAttribute(atributo);
        assert actual.equals(valor);
        ExtentReportManager.logStepWithScreenshot(driver, "Validacion de atributo " + atributo + " con valor " + valor);
    }

    @And("se espera {int} segundos")
    public void esperar(int segundos) throws InterruptedException {
        long milisegundos = segundos * 1000L;
        Thread.sleep(milisegundos);
        ExtentReportManager.logStep("Espero " + segundos + " segundos");
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
            ExtentReportManager.logStepWithScreenshot(driver, "La pagina cargo completamente");
        } catch (TimeoutException e) {
            // Loggear el fallo y relanzar la excepción para que el paso falle en Cucumber
            if (Hooks.scenarioTest != null) {
                Hooks.scenarioTest.fail("La pagina no terminó de cargar tras 15 segundos: " + e.getMessage());
            }
            throw e;
        }
    }

    @And("se cambia a la pestaña numero {int}")
    public void cambiar_a_pestana(int index) {
        int targetIndex = index - 1;
        List<String> pestañas = new ArrayList<>(Hooks.driver.getWindowHandles());

        if (targetIndex < pestañas.size()) {
            Hooks.driver.switchTo().window(pestañas.get(targetIndex));
            ExtentReportManager.logStepWithScreenshot(Hooks.driver, "Se cambio a la pestaña numero: " + index);
        } else {
            // Loggear el error y lanzar una excepción para que el paso falle en Cucumber
            String errorMessage = "No existe la pestaña numero: " + index + ". Numero de pestañas abiertas: " + pestañas.size();
            if (Hooks.scenarioTest != null) {
                Hooks.scenarioTest.fail(errorMessage);
            }
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
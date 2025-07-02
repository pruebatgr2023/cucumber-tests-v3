package runners;

import org.junit.AfterClass; // Importar AfterClass
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import utils.Hooks; // Importar la clase Hooks

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",     // Ruta de los archivos .feature
    glue = {"stepdefs", "utils"},                 // Paquetes donde están tus Steps y Hooks
    plugin = {
        "pretty",                                 // Imprime resultados legibles en consola
        "json:target/cucumber-reports/cucumber.json" // Genera el reporte JSON de Cucumber (útil para otros reportes o análisis)
        // Eliminado: "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true                             // Hace la salida de consola más legible
)
public class TestRunner {

    // Este método se ejecutará una vez después de que todas las pruebas en este TestRunner hayan terminado.
    @AfterClass
    public static void tearDownSuite() {
        // Asegurarse de que el reporte de Extent se escriba al disco
        Hooks.flushExtentReports(); // Llama al método de flush en Hooks
        System.out.println("DEBUG: Todos los escenarios han terminado. Reporte de Extent finalizado.");
    }
}
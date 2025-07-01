package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",     // Ruta de los archivos .feature
    glue = {"stepdefs", "utils"},                 // Paquetes donde están tus Steps y Hooks
    plugin = {
        "pretty",                                 // Imprime resultados legibles en consola
        "html:target/cucumber-reports.html"       // Reporte HTML básico (puedes quitar si usas ExtentReports)
    },
    monochrome = true                             // Hace la salida de consola más legible
)
public class TestRunner {
}

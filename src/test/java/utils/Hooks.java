package utils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest; // Importar ExtentTest
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.Status; // Importar Status para loggear fallos

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks {
    public static WebDriver driver;
    // Ahora ExtentReports y ExtentTest serán gestionados directamente aquí
    public static ExtentReports extent;
    public static ExtentTest scenarioTest; // Para el test del escenario actual

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Iniciando escenario: " + scenario.getName());

        // Inicializar ExtentReports una sola vez por ejecución de suite
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = "target/reportes/Reporte_" + timestamp + ".html";

            File reportDir = new File("target/reportes");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
                System.out.println("DEBUG: Directorio de reportes creado: " + reportDir.getAbsolutePath());
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Reporte de Pruebas Automatizadas");
            sparkReporter.config().setReportName("Resultado Detallado - QA Test");
            sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
            sparkReporter.config().setEncoding("UTF-8");
            sparkReporter.config().setCss(
                    ".nav-wrapper { background-color: #1e1e2f !important; } " +
                    ".brand-logo { font-weight: bold; color: #00e676 !important; } " +
                    ".test-content .card-panel { border-radius: 30px; box-shadow: 0 0 35px #00e5ff; }"
            );

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Tester", "Diego Sanchez");
            extent.setSystemInfo("Entorno", "LOCAL");
            extent.setSystemInfo("Sistema", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            System.out.println("DEBUG: ExtentReports inicializado con reporte en: " + reportPath);
        }

        // Crear un nuevo test para cada escenario
        scenarioTest = extent.createTest(scenario.getName());
        System.out.println("DEBUG: ExtentTest creado para escenario: " + scenario.getName());

        // Configuración de ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-geolocation");
        options.addArguments("--safebrowsing-disable-download-protection");
        options.addArguments("--disable-save-password-bubble");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
      //options.addArguments("user-data-dir=C:/Users/diego/AppData/Local/Google/Chrome/User Data");
      //options.addArguments("profile-directory=Profile 1");
        // Inicializar el WebDriver
        driver = new ChromeDriver(options);
        System.out.println("DEBUG: WebDriver inicializado.");
    }

    @After
    public void tearDown(Scenario scenario) {
        // Si el escenario falla, toma una captura de pantalla y loggea el fallo
        if (scenario.isFailed() && driver != null) {
            System.out.println("DEBUG: Escenario fallido. Intentando tomar captura de pantalla y loggear fallo.");
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                // Adjunta la captura de pantalla directamente al ExtentTest del escenario
                scenarioTest.fail("El escenario falló.", com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(java.util.Base64.getEncoder().encodeToString(screenshot)).build());
                System.out.println("DEBUG: Captura de pantalla adjuntada y fallo loggeado para el escenario: " + scenario.getName());
            } catch (WebDriverException e) {
                System.err.println("ERROR: Error al tomar captura de pantalla en fallo (WebDriver): " + e.getMessage());
                e.printStackTrace();
                scenarioTest.fail("El escenario falló. Error al tomar captura de pantalla: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("ERROR: Error general al loggear fallo con captura: " + e.getMessage());
                e.printStackTrace();
                scenarioTest.fail("El escenario falló. Error inesperado: " + e.getMessage());
            }
        } else if (scenario.getStatus().equals(io.cucumber.java.Status.PASSED)) {
            scenarioTest.pass("El escenario pasó exitosamente.");
        } else {
            scenarioTest.skip("El escenario fue saltado o tuvo otro estado: " + scenario.getStatus().name());
        }

        // Cierra el navegador después de cada escenario
        if (driver != null) {
            driver.quit();
            System.out.println("DEBUG: Navegador cerrado para el escenario: " + scenario.getName());
            driver = null;
        }
    }

    // Método para asegurar que el reporte se escribe al disco al final de la ejecución de la suite
    public static void flushExtentReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("DEBUG: ExtentReports flushed.");
        }
    }
}
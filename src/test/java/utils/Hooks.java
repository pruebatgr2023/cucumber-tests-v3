package utils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Hooks {
    public static WebDriver driver;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);

        ExtentReportManager.setupReport();  // inicia el reporte al comienzo del escenario
        ExtentReportManager.test = ExtentReportManager.extent.createTest("Escenario");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        ExtentReportManager.flushReport(); // finaliza y guarda el reporte
    }
}

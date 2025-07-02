package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentTest; // Importar ExtentTest
import com.aventstack.extentreports.Status; // Importar Status

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64; // Importar Base64 (aunque ya no se usará directamente para adjuntar)
import java.util.Date;

// Esta clase ahora es una utilidad para capturar screenshots y loggear información
// utilizando la instancia de ExtentTest gestionada en Hooks.java.
public class ExtentReportManager {

    // Método para capturar screenshot y guardarla en el directorio de reportes/screenshots
    // Ahora devuelve la ruta relativa de la imagen guardada.
    public static String captureScreenshotAndSave(WebDriver driver, String nombre) {
        String screenshotDir = "target/reportes/screenshots"; // CAMBIO: Capturas dentro de la carpeta de reportes
        String relativePath = "screenshots/" + nombre + ".png"; // Ruta relativa desde el reporte HTML
        String fullPath = screenshotDir + "/" + nombre + ".png";

        System.out.println("DEBUG: Intentando guardar captura en: " + fullPath);

        try {
            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("DEBUG: Directorio de capturas creado: " + screenshotDir);
            }
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(new File(fullPath).toPath(), screenshotBytes);
            System.out.println("DEBUG: Captura de pantalla guardada exitosamente en disco: " + fullPath);
            return relativePath; // Devolvemos la ruta relativa para el reporte
        } catch (WebDriverException e) {
            System.err.println("ERROR: Error al tomar captura de pantalla (WebDriver): " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("ERROR: Error al guardar la captura en disco: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Devolver null si falla
    }

    // Método para loggear un paso con captura de pantalla
    // Ahora adjunta la captura de pantalla referenciándola por su ruta.
    public static void logStepWithScreenshot(WebDriver driver, String message) {
        try {
            if (Hooks.scenarioTest != null && driver != null) {
                String screenshotName = "paso_" + System.currentTimeMillis();
                String savedPath = captureScreenshotAndSave(driver, screenshotName); // Guardar y obtener ruta relativa

                if (savedPath != null) {
                    // Adjunta la captura de pantalla al ExtentTest del escenario referenciando el archivo
                    Hooks.scenarioTest.info(message, MediaEntityBuilder.createScreenCaptureFromPath(savedPath).build());
                    System.out.println("DEBUG: Captura de pantalla adjuntada al reporte por ruta: " + savedPath);
                } else {
                    Hooks.scenarioTest.info(message + " (No se pudo tomar o guardar captura)");
                    System.err.println("ERROR: No se pudo guardar la captura para el paso: " + message);
                }
            } else if (Hooks.scenarioTest != null) {
                Hooks.scenarioTest.info(message);
                System.out.println("DEBUG: Mensaje de paso loggeado (sin driver): " + message);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Error general al loggear paso con captura: " + message + " - " + e.getMessage());
            e.printStackTrace();
            if (Hooks.scenarioTest != null) {
                Hooks.scenarioTest.warning("Advertencia: Error inesperado al loggear paso: " + message + " - " + e.getMessage());
            }
        }
    }

    // Método para loggear un paso sin captura de pantalla
    // Ahora usa Hooks.scenarioTest para loggear.
    public static void logStep(String message) {
        if (Hooks.scenarioTest != null) {
            Hooks.scenarioTest.info(message);
            System.out.println("DEBUG: Mensaje de paso loggeado: " + message);
        }
    }
}

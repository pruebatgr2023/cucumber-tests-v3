package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {

    public static ExtentReports extent;
    public static ExtentTest test;
    public static String reportPath;

    public static void setupReport() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        reportPath = "target/reportes/Reporte_" + timestamp + ".html";

        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

        // Estilo visual atractivo
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setDocumentTitle("📊 Reporte de Pruebas Automatizadas");
        reporter.config().setReportName("🔥 Resultado Detallado - QA Test");
        reporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
        reporter.config().setEncoding("UTF-8");
        reporter.config().setCss(
                ".nav-wrapper { background-color: #1e1e2f !important; } " +
                ".brand-logo { font-weight: bold; color: #00e676 !important; } " +
                ".test-content .card-panel { border-radius: 10px; box-shadow: 0 0 15px #00e5ff; }"
        );

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        // Información del sistema
        extent.setSystemInfo("👨🏻‍💻 Tester", "Diego Sánchez");
        extent.setSystemInfo("🌐 Entorno", "LOCAL");
        extent.setSystemInfo("⚙️ Sistema", System.getProperty("os.name"));
        extent.setSystemInfo("☕ Java", System.getProperty("java.version"));
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            // Abre automáticamente el HTML
            try {
                java.awt.Desktop.getDesktop().browse(new File(reportPath).toURI());
            } catch (IOException e) {
                System.err.println("❌ No se pudo abrir el reporte automáticamente.");
                e.printStackTrace();
            }
        }
    }

    public static String captureScreenshot(WebDriver driver, String nombre) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotDir = "target/screenshots";
        String relativePath = "../screenshots/" + nombre + ".png";
        String fullPath = screenshotDir + "/" + nombre + ".png";

        try {
            new File(screenshotDir).mkdirs();
            Files.copy(src.toPath(), new File(fullPath).toPath());
        } catch (IOException e) {
            System.err.println("❌ Error al guardar la captura: " + e.getMessage());
        }

        return relativePath;
    }

    // logStep con screenshot
    public static void logStep(WebDriver driver, String mensaje) {
        try {
            if (driver != null) {
                String ruta = captureScreenshot(driver, "paso_" + System.currentTimeMillis());
                test.info("🟢 " + mensaje, MediaEntityBuilder.createScreenCaptureFromPath(ruta).build());
            } else {
                test.info("🔵 " + mensaje);
            }
        } catch (Exception e) {
            test.warning("⚠️ Error al registrar paso: " + mensaje + " - " + e.getMessage());
        }
    }

    // logStep sin WebDriver (sin imagen)
    public static void logStep(String mensaje) {
        if (test != null) {
            test.info("🔵 " + mensaje);
        }
    }
}
package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
//import java.util.Base64;

public class ScreenshotUtil {
    public static String takeScreenshot(WebDriver driver, String name) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File("target/screenshots/" + name + ".png");
        dest.getParentFile().mkdirs();
        try {
            Files.copy(screenshot.toPath(), dest.toPath());
            return "<img src='screenshots/" + name + ".png' width='400'/>";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
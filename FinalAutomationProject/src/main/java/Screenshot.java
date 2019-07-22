import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Screenshot {
    private static final String folderPath = ".\\screenshots\\";

    @Attachment(type = "image/png")
    public static byte[] saveScreenshotPNG(WebDriver driver, String actualMethod) {
        byte[] capturedScreenshot;
        try {
            File screenshotFolder = new File(folderPath);
            if(!screenshotFolder.canExecute()){
                screenshotFolder.mkdir();
            }
            capturedScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String name = folderPath + actualMethod + ".png";
            FileOutputStream file = new FileOutputStream(name);
            file.write(capturedScreenshot);
            file.close();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot create screenshot;", ex);
        }
        return capturedScreenshot;
    }
}

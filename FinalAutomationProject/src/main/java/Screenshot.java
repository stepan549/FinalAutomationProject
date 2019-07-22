import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.FileOutputStream;

public class Screenshot {
    private static final String folderPath = "D:\\ServiceITGit\\FinalAutomationProject\\screenshots\\";

    @Attachment(type = "image/png")
    public static byte[] saveScreenshotPNG(WebDriver driver, String actualMethod) {
        byte[] capturedScreenshot;
        try {
            capturedScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String name = folderPath + actualMethod + ".png";
            FileOutputStream file = new FileOutputStream(name);
            file.write(capturedScreenshot);
            file.close();
        }
        catch (Exception ex) {
            throw new RuntimeException("cannot create screenshot;", ex);
        }
        return capturedScreenshot;
    }
}

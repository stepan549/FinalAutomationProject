package patterns;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverSingleton {
    private static WebDriver DRIVER;
    private static String DRIVER_FILE = "chromedriver.exe";
    private static String KEY = "webdriver.chrome.driver";

    private DriverSingleton() {
    }

    public static WebDriver getInstance() {
        if (DRIVER == null) {
            System.setProperty(KEY, DRIVER_FILE);
            DRIVER = new ChromeDriver();
        }
        return DRIVER;
    }

    public static void quit(){
        DRIVER.quit();
        DRIVER = null;
    }
}

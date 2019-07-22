import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import db.DatabaseOperations;
import model.SettingsData;
import model.UserData;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import pages.LoginPage;
import parser.Parser;
import parser.SettingParameter;
import parser.UseParser;
import patterns.DriverSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private Eyes eyes;
    private static String MAIN_PAGE;
    private static String LOGIN;
    private static String PASSWORD;
    private static String INCORRECT_PASSWORD;
    private static int PAGE_LOAD_TIMEOUT;
    private static int GET_LOGIN;
    private static String API_KEY = "CYFkrD5pcIcBJCNFtJ2Yub6ybmvsZuvjisYmskeoi24110";
    private static final Logger logger = Logger.getLogger(LoginTest.class);


    @BeforeClass
    public void setUpDriver() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        SettingsData settingsData = Parser.getSettingData(getClass().getName(), UseParser.STAX);
        MAIN_PAGE = (String) SettingParameter.getParameter("MAIN_PAGE", settingsData);
        logger.info("Get parameter MAIN_PAGE: " + MAIN_PAGE);
        PAGE_LOAD_TIMEOUT = Integer.parseInt((String) SettingParameter.getParameter("PAGE_LOAD_TIMEOUT", settingsData));
        logger.info("Get parameter PAGE_LOAD_TIMEOUT: " + PAGE_LOAD_TIMEOUT);
        GET_LOGIN = Integer.parseInt((String) SettingParameter.getParameter("GET_LOGIN", settingsData));
        logger.info("Get parameter GET_LOGIN: " + GET_LOGIN);
        INCORRECT_PASSWORD = (String) SettingParameter.getParameter("INCORRECT_PASSWORD", settingsData);
        logger.info("Get parameter INCORRECT_PASSWORD: " + INCORRECT_PASSWORD);
        DatabaseOperations databaseOperations = new DatabaseOperations();
        UserData userData = databaseOperations.getAccessData(GET_LOGIN);
        logger.info("Request login data by ID = " + GET_LOGIN);
        LOGIN = userData.getLogin();
        PASSWORD = userData.getPassword();
        logger.info("Response data: login " + LOGIN + ", password " + PASSWORD);
    }

    @BeforeMethod
    public void before() {
        eyes = new Eyes();
        eyes.setApiKey(API_KEY);
        driver = DriverSingleton.getInstance();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.get(MAIN_PAGE);
        loginPage = new LoginPage(driver);
        logger.info("Open page " + MAIN_PAGE);
    }

    @AfterMethod
    public void after(ITestResult result) {
        Screenshot.saveScreenshotPNG(driver, result.getMethod().getMethodName());
        DriverSingleton.quit();
        eyes.abortIfNotClosed();
    }

    @Test
    public void positiveLogin() {
        eyes.open(driver, "MAIL TEST", "Login positive");
        loginPage.login(LOGIN, PASSWORD);
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        eyes.checkElement(By.id("PH_logoutLink"));
        TestResults testResults = eyes.close();
        if (testResults.isNew()) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(testResults.isPassed());
        }
        //Assert.assertTrue(loginPage.isLogoutLinkDisplayed());
    }

    @Test
    public void negativeLogin() {
        eyes.open(driver, "MAIL TEST", "Login negative");
        loginPage.failLogin(LOGIN, INCORRECT_PASSWORD);
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        eyes.checkElement(By.id("mailbox:error"));
        TestResults testResults = eyes.close();
        if (testResults.isNew()) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(testResults.isPassed());
        }
        //Assert.assertTrue(loginPage.idErrorDisplayed());
    }
}

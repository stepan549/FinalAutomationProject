import db.DatabaseOperations;
import model.SettingsData;
import model.UserData;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import pages.LoginPage;
import pages.MailboxPage;
import parser.Parser;
import parser.SettingParameter;
import parser.UseParser;
import patterns.DriverSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FilterTest {
    private WebDriver driver;
    private MailboxPage mailboxPage;
    private static String MAIN_PAGE;
    private static String LOGIN;
    private static String PASSWORD;
    private static int PAGE_LOAD_TIMEOUT;
    private static int GET_LOGIN;
    private static final Logger logger = Logger.getLogger(FilterTest.class);

    public FilterTest() {
    }

    @BeforeClass
    public void setUp() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        SettingsData settingsData = Parser.getSettingData(getClass().getName(), UseParser.DOM);
        MAIN_PAGE = (String) SettingParameter.getParameter("MAIN_PAGE", settingsData);
        logger.info("Get parameter MAIN_PAGE: " + MAIN_PAGE);
        PAGE_LOAD_TIMEOUT = Integer.parseInt((String) SettingParameter.getParameter("PAGE_LOAD_TIMEOUT", settingsData));
        logger.info("Get parameter PAGE_LOAD_TIMEOUT: " + PAGE_LOAD_TIMEOUT);
        GET_LOGIN = Integer.parseInt((String) SettingParameter.getParameter("GET_LOGIN", settingsData));
        logger.info("Get parameter GET_LOGIN: " + GET_LOGIN);
        DatabaseOperations databaseOperations = new DatabaseOperations();
        UserData userData = databaseOperations.getAccessData(GET_LOGIN);
        logger.info("Request login data by ID = " + GET_LOGIN);
        LOGIN = userData.getLogin();
        PASSWORD = userData.getPassword();
        logger.info("Response data: login " + LOGIN + ", password " + PASSWORD);
    }

    @BeforeMethod
    public void setUpBrowser() {
        driver = DriverSingleton.getInstance();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.get(MAIN_PAGE);
        logger.info("Open page " + MAIN_PAGE);
    }

    @Test
    public void filteredByFlagTest() {
        mailboxPage = new LoginPage(driver).enterLogin(LOGIN).enterPassword(PASSWORD).login();
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        mailboxPage.markThreeMailsByFlag();
        logger.info("Mark three mail's checkboxes");
        mailboxPage.clickFilterByFlag();
        logger.info("Click filter by flags");
        Assert.assertTrue(mailboxPage.getCountMarkedByFlags() == 3);
    }

    @AfterMethod
    public void closeBrowser(ITestResult result) {
        Screenshot.saveScreenshotPNG(driver, result.getMethod().getMethodName());
        mailboxPage.clickFilterByFlag();
        mailboxPage.unmarkAllFlags();
        DriverSingleton.quit();
    }
}

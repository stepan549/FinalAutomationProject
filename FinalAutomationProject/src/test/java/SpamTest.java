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

public class SpamTest {

    private WebDriver driver;
    private static String LOGIN;
    private static String PASSWORD;
    private static String MAIN_PAGE;
    private static int PAGE_LOAD_TIMEOUT;
    private static int GET_LOGIN;
    private static final Logger logger = Logger.getLogger(SpamTest.class);

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
        logger.info("Request login data by ID = " + GET_LOGIN);
        UserData userData = databaseOperations.getAccessData(GET_LOGIN);
        LOGIN = userData.getLogin();
        PASSWORD = userData.getPassword();
        logger.info("Response data: login " + LOGIN + ", password " + PASSWORD);
    }

    @BeforeMethod
    public void before() {
        driver = DriverSingleton.getInstance();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.get(MAIN_PAGE);
        logger.info("Open page " + MAIN_PAGE);
    }

    @AfterMethod
    public void after(ITestResult result) {
        Screenshot.saveScreenshotPNG(driver, result.getMethod().getMethodName());
        DriverSingleton.quit();
    }

    @Test
    public void sendToSpam() {
        MailboxPage mailboxPage = new LoginPage(driver).enterLogin(LOGIN).enterPassword(PASSWORD).login();
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        mailboxPage.checkFirstMessageByCheckbox();
        logger.info("Mark first mail's checkbox");
        mailboxPage.sendToSpam();
        logger.info("Click spam button");
        Assert.assertTrue(mailboxPage.isSendToSpamMessageDisplayed());
    }

    @Test
    public void returnFromSpam() {
        MailboxPage mailboxPage = new LoginPage(driver).enterLogin(LOGIN).enterPassword(PASSWORD).login();
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        mailboxPage.clickSpam();
        logger.info("Open spam page");
        mailboxPage.returnFirstMessageFromSpam();
        logger.info("Return first message from spam");
        Assert.assertTrue(mailboxPage.isReturnToInboxMessageDisplayed());
    }
}

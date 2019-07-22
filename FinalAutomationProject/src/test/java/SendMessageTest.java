import db.DatabaseOperations;
import model.MessageData;
import model.SettingsData;
import model.UserData;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.xml.sax.SAXException;
import pages.ComposePage;
import pages.LoginPage;
import pages.MailboxPage;
import pages.MessagePage;
import parser.Parser;
import parser.SettingParameter;
import parser.UseParser;
import patterns.DriverSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SendMessageTest {
    private WebDriver driver;
    private MailboxPage mailboxPage;
    private ComposePage composePage;
    private MessagePage messagePage;
    private static String LOGIN;
    private static String PASSWORD;
    private static String MAIN_PAGE;
    private static int PAGE_LOAD_TIMEOUT;
    private static int GET_LOGIN;
    private static final Logger logger = Logger.getLogger(SendMessageTest.class);

    @BeforeClass
    public void setUpDriver() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        SettingsData settingsData = Parser.getSettingData(getClass().getName(), UseParser.SAX);
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

    @Test(dataProvider = "getMessages")
    public void sendMessage(String mailTo, String subject, String message) {
        mailboxPage = new LoginPage(driver).enterLogin(LOGIN).enterPassword(PASSWORD).login();
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        composePage = mailboxPage.clickWriteMessageButton();
        logger.info("Click new message");
        composePage.writeMessageTo(mailTo);
        logger.info("Write send message to " + mailTo);
        composePage.writeSubject(subject);
        logger.info("Write subject " + subject);
        composePage.writeMessage(message);
        logger.info("Write message " + message);
        composePage.sendMessage();
        logger.info("Send message");
        Assert.assertTrue(composePage.isSendMessageDisplayed());
    }

    @DataProvider(name = "getMessages")
    public Object[][] getMessages() {
        DatabaseOperations databaseOperations = new DatabaseOperations();
        List<MessageData> messageData = databaseOperations.getMessages(LOGIN);
        Object[][] objectArray = new Object[messageData.size()][3];
        for (int i = 0; i < messageData.size(); i++) {
            objectArray[i][0] = messageData.get(i).getSendTo();
            objectArray[i][1] = messageData.get(i).getSubject();
            objectArray[i][2] = messageData.get(i).getMessage();
        }
        return objectArray;
    }

    @Test(dataProvider = "getMessages")
    public void checkInDelivered(String mailTo, String subject, String message) {
        mailboxPage = new LoginPage(driver).enterLogin(LOGIN).enterPassword(PASSWORD).login();
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        composePage = mailboxPage.clickWriteMessageButton();
        logger.info("Click new message");
        logger.info("Write send message to " + mailTo);
        composePage.writeSubject(subject);
        logger.info("Write subject " + subject);
        composePage.writeMessage(message);
        logger.info("Write message " + message);
        composePage.sendMessage();
        logger.info("Send message");
        mailboxPage.clickDeliveredButton();
        logger.info("Open delivered page");
        messagePage = mailboxPage.openMailBySubject(subject);
        logger.info("Open mail by subject " + subject);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(messagePage.getSubject(), subject);
        softAssert.assertEquals(messagePage.getDeliveredTo(), mailTo);
        softAssert.assertEquals(messagePage.getMessage(), message);
        softAssert.assertAll();
    }

    @Test
    public void sendToDraft() {
        mailboxPage = new LoginPage(driver).enterLogin(LOGIN).enterPassword(PASSWORD).login();
        logger.info("Log in via login " + LOGIN + " and password " + PASSWORD);
        messagePage = mailboxPage.openFirstMail();
        logger.info("Open first mail");
        messagePage.clickSendToDraft();
        logger.info("Send mail to draft");
        Assert.assertTrue(messagePage.isDisplayedDraftMessage());
    }
}

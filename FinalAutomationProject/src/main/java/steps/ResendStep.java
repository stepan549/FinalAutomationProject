package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import db.DatabaseOperations;
import model.SettingsData;
import model.UserData;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
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

public class ResendStep {
    private WebDriver driver;
    private LoginPage loginPage;
    private MailboxPage mailboxPage;
    private MessagePage messagePage;
    private ComposePage composePage;
    private static String MAIN_PAGE;
    private static String LOGIN;
    private static String PASSWORD;
    private static int GET_LOGIN;
    private static final Logger logger = Logger.getLogger(ResendStep.class);

    public ResendStep() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        SettingsData settingsData = Parser.getSettingData(getClass().getName(), UseParser.STAX);
        MAIN_PAGE = (String) SettingParameter.getParameter("MAIN_PAGE", settingsData);
        logger.info("Get parameter MAIN_PAGE: " + MAIN_PAGE);
        GET_LOGIN = Integer.parseInt((String) SettingParameter.getParameter("GET_LOGIN", settingsData));
        logger.info("Get parameter GET_LOGIN: " + GET_LOGIN);
        DatabaseOperations databaseOperations = new DatabaseOperations();
        UserData userData = databaseOperations.getAccessData(GET_LOGIN);
        logger.info("Request login data by ID = " + GET_LOGIN);
        LOGIN = userData.getLogin();
        PASSWORD = userData.getPassword();
        logger.info("Response data: login " + LOGIN + ", password " + PASSWORD);
        driver = DriverSingleton.getInstance();
        driver.manage().window().maximize();
        driver.get(MAIN_PAGE);
        logger.info("Open page " + MAIN_PAGE);
    }

    @Given("^I am in mailbox page$")
    public void openMailbox() {
        loginPage = new LoginPage(driver);
        mailboxPage = loginPage.login(LOGIN, PASSWORD);
        logger.info("Open mailbox page");
    }

    @When("^I open first mail$")
    public void openFirstMail() {
        messagePage = mailboxPage.openFirstMail();
        logger.info("Open first mail");
    }

    @When("^I click resend button$")
    public void clickResend() {
        composePage = messagePage.clickResend();
        logger.info("Click resend button");
    }

    @When("^I enter \"([^\"]*)\"$")
    public void enterEmail(String email) {
        composePage.writeMessageTo(email);
        logger.info("Enter mail to " + email);
    }

    @When("^I click to send$")
    public void clickSend() {
        composePage.resendMessage();
        logger.info("Send message");
    }

    @Then("^I see message about send mail$")
    public void checkSendMessage() {
        Assert.assertTrue(composePage.isSendMessageDisplayed());
    }

    @After
    public void after() {
        DriverSingleton.quit();
    }
}

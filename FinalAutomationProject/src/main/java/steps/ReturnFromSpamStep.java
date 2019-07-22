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
import pages.LoginPage;
import pages.MailboxPage;
import parser.Parser;
import parser.SettingParameter;
import parser.UseParser;
import patterns.DriverSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class ReturnFromSpamStep {
    private WebDriver driver;
    private LoginPage loginPage;
    private MailboxPage mailboxPage;
    private static String MAIN_PAGE;
    private static String LOGIN;
    private static String PASSWORD;
    private static int GET_LOGIN;
    private static final Logger logger = Logger.getLogger(ResendStep.class);

    public ReturnFromSpamStep() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
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

    @Given("^I am in mailbox$")
    public void openMailboxPage() {
        loginPage = new LoginPage(driver);
        mailboxPage = loginPage.login(LOGIN, PASSWORD);
        logger.info("Open mailbox page");
    }

    @When("^I check first mail$")
    public void checkFirstMail() {
        mailboxPage.checkFirstMessageByCheckbox();
        logger.info("Mark first message by checkbox");
    }

    @When("^I click spam button$")
    public void clickSpamButton() {
        mailboxPage.sendToSpam();
        logger.info("Click on spam button");
    }

    @When("^I open spam folder$")
    public void openSpamFolder() {
        mailboxPage.clickSpam();
        logger.info("Open spam folder");
    }

    @When("^I return mail from spam$")
    public void clickNoSpam() {
        mailboxPage.returnFirstMessageFromSpam();
        logger.info("Click to button from spam");
    }

    @Then("^I see message return mail from spam$")
    public void checkReturnMessage() {
        Assert.assertTrue(mailboxPage.isReturnToInboxMessageDisplayed());
    }

    @After
    public void after() {
        DriverSingleton.quit();
    }
}

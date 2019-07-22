package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private static int WAIT_TIMEOUT = 300;

    @FindBy(id = "mailbox:error")
    private WebElement errorMessage;

    @FindBy(id = "mailbox:login")
    private WebElement loginField;

    @FindBy(id = "mailbox:password")
    private WebElement passwordField;

    @FindBy(id = "PH_logoutLink")
    private WebElement logoutLink;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public MailboxPage login(String login, String password) {
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        passwordField.submit();
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(logoutLink));
        return new MailboxPage(driver);
    }

    public LoginPage enterLogin(String login){
        loginField.sendKeys(login);
        return this;
    }

    public LoginPage enterPassword(String password){
        passwordField.sendKeys(password);
        return this;
    }

    public void failLogin(String login, String password){
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        passwordField.submit();
    }

    public MailboxPage login(){
        passwordField.submit();
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(logoutLink));
        return new MailboxPage(driver);
    }

    public boolean isLogoutLinkDisplayed() {
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(logoutLink));
        return logoutLink.isDisplayed();
    }

    public boolean idErrorDisplayed() {
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.isDisplayed();
    }
}

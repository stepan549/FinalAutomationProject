package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MessagePage {
    private WebDriver driver;
    private static int WAIT_TIMEOUT = 60;

    @FindBy(xpath = "//div[@class=\"b-letter__head__subj__text\"]")
    private WebElement mailSubject;

    @FindBy(xpath = "//span[@class=\"b-letter__head__addrs__value\"]/span/span[1]")
    private WebElement deliveredTo;

    @FindBy(xpath = "//div[@class=\"b-letter__body\"]//div[contains(@class,'class')]")
    private WebElement mailMessage;

    @FindBy(xpath = "//i[contains(@class,'ico_letter_more')]")
    private WebElement messageMore;

    @FindBy(xpath = "//div[@class=\"b-dropdown__list__item\" and @data-name=\"remove\"]")
    private WebElement removeLink;

    @FindBy(xpath = "//div[contains(@class,\"b-dropdown__list__item\") and @data-name=\"moveTo\"]")
    private WebElement replaceLink;

    @FindBy(xpath = "//a[@data-bem=\"b-dropdown__list__params\" and @data-text=\"Черновики\"]")
    private WebElement draftLink;

    @FindBy(xpath = "//span[contains(text(),'Удалено 1 письмо.')]")
    private WebElement deleteMessage;

    @FindBy(xpath = "//span[contains(text(),'Письмо перемещено в папку «Черновики».')]")
    private WebElement draftMessage;

    @FindBy(xpath = "//div[@class=\"b-dropdown__list__item\" and @data-name=\"forward\"]")
    private WebElement resendLink;


    public MessagePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getSubject() {
        return (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(mailSubject))
                .getText();
    }

    public String getDeliveredTo() {
        return (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(deliveredTo))
                .getText();
    }

    public String getMessage() {
        return (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(mailMessage))
                .getText();
    }

    public void clickDeleteMessage(){
        messageMore.click();
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(removeLink)).click();
    }

    public boolean isDisplayedDeleteMessage(){
        return (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(deleteMessage)).isDisplayed();
    }

    public boolean isDisplayedDraftMessage(){
        return (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(draftMessage)).isDisplayed();
    }

    public void clickSendToDraft(){
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(messageMore))
                .click();
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(replaceLink))
                .click();
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(draftLink))
                .click();
    }

    public ComposePage clickResend(){
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(messageMore))
                .click();
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(resendLink))
                .click();
        return new ComposePage(driver);
    }
}

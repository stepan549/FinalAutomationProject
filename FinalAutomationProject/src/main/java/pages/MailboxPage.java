package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MailboxPage {

    private WebDriver driver;
    private static int WAIT_TIMEOUT = 60;

    @FindBy(xpath = "//a[contains(@class,'js-href')][1]")
    private WebElement mail;

    @FindBy(xpath = "//div[contains(@class,'b-datalist__item__subj')][1]")
    private WebElement mailSubject;

    @FindBy(xpath = "(//div[contains(@class,'b-datalist__item__subj')]/span)[1]")
    private WebElement shortMessage;

    @FindBy(xpath = "(//div[contains(@class,'b-datalist__item__addr')])[1]")
    private WebElement mailFrom;

    @FindBy(xpath = "//*[contains(text(),'Письмо перемещено в папку «Спам».')]")
    private WebElement notifySendToSpamMessage;

    @FindBy(xpath = "//*[contains(text(),'Письмо перемещено в папку «Входящие»')]")
    private WebElement notifyReturnToInboxMessage;

    @FindBy(xpath = "//div[contains(@class,'b-datalist__item__cbx')]//div[@class='b-checkbox__box']")
    private WebElement mailCheckbox;

    @FindBy(xpath = "//div[@data-name='spam'][1]")
    private WebElement spamButton;

    @FindBy(xpath = "//div[@data-name='noSpam'][1]")
    private WebElement noSpamButton;

    @FindBy(xpath = "(//a[@data-name='compose'])[1]")
    private WebElement writeMessageButton;

    @FindBy(xpath = "//a[@class='b-nav__link']/span[text()='Спам']")
    private WebElement spamFolderLink;

    @FindBy(xpath = "//a[@class='b-nav__link']/span[text()='Входящие']")
    private WebElement inboxFolderLink;

    @FindBy(xpath = "(//div[@data-name=\"flagged\"])[1]")
    private WebElement filterByFlagButton;

    @FindBy(xpath = "//span[text()='Отправленные']")
    private WebElement deliveredButton;

    @FindBy(xpath = "//*[text()='Корзина']")
    private WebElement trashButton;

    @FindBy(xpath = "//a[@class=\"portal-footer__link\"]/span[text()='Настройки']")
    private WebElement settingsLink;

    @FindBy(id = "PH_logoutLink")
    private WebElement logoutLink;

    public MailboxPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public MainPage clickLogoutLink() {
        logoutLink.click();
        return new MainPage(driver);
    }

    public void checkFirstMessageByCheckbox() {
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(mailCheckbox)).click();
    }

    public void sendToSpam() {
        spamButton.click();
    }

    public boolean isSendToSpamMessageDisplayed() {
        return (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(notifySendToSpamMessage)).isDisplayed();
    }

    public void clickSpam() {
        spamFolderLink.click();
    }

    public void returnFirstMessageFromSpam() {
        //WebElement spamMessage = driver.findElement(By.xpath("(//div[contains(@class,'b-datalist__item__cbx')]//div[@class='b-checkbox__box'])[26]"));
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@class,'b-datalist__item__cbx')]//div[@class='b-checkbox__box'])[26]"))).click();
        noSpamButton.click();
    }

    public boolean isReturnToInboxMessageDisplayed() {
        return (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(notifyReturnToInboxMessage)).isDisplayed();
    }

    public ComposePage clickWriteMessageButton() {
        writeMessageButton.click();
        return new ComposePage(driver);
    }

    public void markThreeMailsByFlag() {
        int selectCount = 3;
        for (int i = 1; i <= selectCount; i++) {
            driver.findElement(By.xpath("(//div[@data-act='flag'])[" + i + "]")).click();
        }
    }

    public void unmarkAllFlags() {
        List<WebElement> mailsFlag = driver.findElements(By.xpath("//div[contains(@class,'b-flag_yes')]"));
        for (WebElement flagElement : mailsFlag) {
            flagElement.click();
        }
    }

    public int getCountMarkedByFlags() {
        return driver.findElements(By.xpath("//div[contains(@class,'b-flag_yes')]")).size();
    }

    public int getCountMessagesFilteredByFlag() {
        return driver.findElements(By.xpath("//div[@class=\"b-datalist__item__body\"]")).size();
    }

    public void clickFilterByFlag() {
        filterByFlagButton.click();
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='b-datalist__item__body'])[1]")));
    }

    public void clickDeliveredButton() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(deliveredButton)).click();
    }

    public MessagePage openMailBySubject(String subject) {
        String xpathDeliveredMail = String.format("//*[text()='%s']", subject);
        System.out.println(xpathDeliveredMail);
        WebElement mail = driver.findElement(By.xpath(xpathDeliveredMail));
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(mail));
        mail.click();
        return new MessagePage(driver);
    }

    public void clickInboxButton() {
        inboxFolderLink.click();
    }

    public MessagePage openFirstMail() {
        mail.click();
        return new MessagePage(driver);
    }

    public void clickTrashButton() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(trashButton)).click();
    }

    public MailSettingsPage clickSettings() {
        settingsLink.click();
        return new MailSettingsPage(driver);
    }
}

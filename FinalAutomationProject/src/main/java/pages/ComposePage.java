package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public class ComposePage {

    private WebDriver driver;

    @FindBy(xpath = "//span[text()='Кому']")
    private WebElement contactsLabel;

    @FindBy(name = "Subject")
    private WebElement subjectInput;

    @FindBy(xpath = "//span[text()='Мои контакты']")
    private WebElement contactsGroup;

    @FindBy(xpath = "//div[@class='b-explorer-header']//div[@class='b-checkbox__box']")
    private WebElement selectAllContactsCheckbox;

    @FindBy(xpath = "//button/span[text()='Добавить']")
    private WebElement addContactsButton;

    @FindBy(xpath = "(//span[text()='Отправить'])[1]")
    private WebElement sendButton;

    @FindBy(xpath = "(//span[text()='Отправить'])[4]")
    private WebElement resendButton;

    @FindBy(xpath = "//div[text()='Ваше ']")
    private WebElement sendEmailMessage;

    @FindBy(xpath = "//div[@data-blockid=\"compose_to\"]//textarea[2]")
    private WebElement mailToInput;

    public ComposePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openContacts() {
        contactsLabel.click();
        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
        fluentWait.ignoring(NoSuchElementException.class);
        fluentWait.pollingEvery(Duration.ofMillis(250));
        fluentWait.withTimeout(Duration.ofSeconds(5));
        Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(By.xpath("//span[@class='b-explorer__header__text']")).isDisplayed();
            }
        };
        fluentWait.until(function);
    }

    public void addGroupContacts() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(contactsGroup)).click();
        selectAllContactsCheckbox.click();
        addContactsButton.click();
    }

    public void writeMessageTo(String to){
        mailToInput.sendKeys(to);
    }

    public void writeSubject(String subject) {
        subjectInput.sendKeys(subject);
    }

    public void writeMessage(String message) {
        WebElement frameElement = driver.findElement(By.xpath("//iframe[starts-with(@id,'toolkit')]"));
        driver.switchTo().frame(frameElement);
        driver.findElement(By.id("tinymce")).click();
        driver.findElement(By.id("tinymce")).clear();
        driver.findElement(By.id("tinymce")).sendKeys(message);
        driver.switchTo().defaultContent();
    }

    public void sendMessage() {
        sendButton.click();
    }

    public void resendMessage() {
        resendButton.click();
    }

    public boolean isSendMessageDisplayed() {
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOf(sendEmailMessage))
                .isDisplayed();
    }
}

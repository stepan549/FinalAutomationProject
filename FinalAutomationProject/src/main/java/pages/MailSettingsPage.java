package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MailSettingsPage {
    private WebDriver driver;
    private static int WAIT_TIMEOUT = 60;

    @FindBy(xpath = "//span[text()='Папки']")
    private WebElement foldersLink;

    @FindBy(xpath = "//button/span[text()='Добавить папку']")
    private WebElement addFolderButton;

    @FindBy(xpath = "//button/span[text()='Удалить']")
    private WebElement removeButton;

    @FindBy(xpath = "//input[@name='name']")
    private WebElement nameFolderInput;

    @FindBy(xpath = "//button/span[text()='Добавить']")
    private WebElement addButton;


    public MailSettingsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickFolderLink() {
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.elementToBeClickable(foldersLink)).click();
    }

    public void addFolder(String folder_name) {
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.elementToBeClickable(addFolderButton)).click();
        (new WebDriverWait(driver, WAIT_TIMEOUT)).until(ExpectedConditions.visibilityOf(nameFolderInput)).sendKeys(folder_name);
        addButton.click();
    }

    public boolean checkAddedFolder(String folder_name) {
        String folderXpath = String.format("//div[text()='%s']", folder_name);
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(folderXpath)))
                .isDisplayed();
    }

    public void removeFolder(String folder_name) {
        String folderXpath = String.format("//div[text()='%s']", folder_name);
        Actions actions = new Actions(driver);
        WebElement folderElement = driver.findElement(By.xpath(folderXpath));
        actions.moveToElement(folderElement).build().perform();
        String folderDeleteXpath = String.format("(//div[text()='%s']//following::i[contains(@class,'ico_folder_delete')])[1]", folder_name);
        WebElement deleteFolder = driver.findElement(By.xpath(folderDeleteXpath));
        deleteFolder.click();
        (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(removeButton))
                .click();
    }
}

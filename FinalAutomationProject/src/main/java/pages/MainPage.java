package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private WebDriver driver;
    private static int WAIT_TIMEOUT = 60;

    @FindBy(id = "PH_regLink")
    private WebElement regestrationLink;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkRegistrationLink() {
        return (new WebDriverWait(driver, WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(regestrationLink))
                .isDisplayed();
    }
}

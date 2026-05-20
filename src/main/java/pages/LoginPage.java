package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    By username = By.cssSelector("input[formcontrolname='username']");
    By password = By.cssSelector("input[formcontrolname='password']");
    By loginBtn = By.xpath("//button[@type='submit' and normalize-space()='Login']");
    By otpField = By.cssSelector("input[formcontrolname='otp']");
    By verifyBtn = By.xpath("//button[@type='submit' and normalize-space()='Verify']");
    By accountDropdown = By.id("org");

    // لقطة زرار الـ Submit
    By submitBtn = By.xpath("//button[@type='submit' and normalize-space()='Submit']");
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    public void login(String user, String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        safeClick(loginBtn);
    }

    public void enterOtp(String otp) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(otpField));
        driver.findElement(otpField).sendKeys(otp);
        safeClick(verifyBtn);
    }

    private void safeClick(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("spinner-overlay")));

        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator));

        element.click();
    }
    public void selectAccountAndSubmit(String accountName) {
        WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(accountDropdown));
        Select select = new Select(selectElement);
        select.selectByVisibleText(accountName);
        safeClick(submitBtn);
    }
}
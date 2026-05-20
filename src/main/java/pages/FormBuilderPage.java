package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FormBuilderPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // 1. الـ Locators الدقيقة جداً
    private By contactInfoToggle = By.xpath("//div[contains(., 'Contact Info')]//input[@type='checkbox']");
    private By saveBtn = By.cssSelector(
            "#tabs-0-panel-app-miscellaneous-tab > app-miscellaneous-tab > form > div.d-flex > button"
    );
    public FormBuilderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    // 2. ميثود الـ Toggle
    public void disableContactInfo() {
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(contactInfoToggle));

        if (toggle.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
            System.out.println("✅ تم قفل الـ Contact Info بنجاح!");
        } else {
            System.out.println("ℹ️ الـ Contact Info مقفول بالفعل.");
        }
    }
    // 3. ميثود الـ Save الحقيقية
    public void clickSave() {
        // بنستنى زرار السيف المحدد يظهر ويبقى Clickable
        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));

        // بنحرك الشاشة عليه للتأكيد
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", save);

        // بندوس عليه عادي بالـ click العادية بتاعة سلينيوم عشان لو فيه أي مشكلة التيست يقف ويقولنا
        save.click();
        System.out.println("✅ تم الضغط على زرار Save الحقيقي بنجاح!");
    }
}
package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new"); // مهم للـ pipeline
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // 1. منع ظهور الـ Notifications Pop-ups تماماً من الـ Arguments
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        // 2. حركة صايعة لتأكيد عمل Block للـ Notification settings جوه كربت المتصفح نفسه
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2); // رقم 2 يعني Block
        options.setExperimentalOption("prefs", prefs);

        // تشغيل الـ Driver بالـ Options الجديدة المتنظفة
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterMethod
    public void teardown() {
        // driver.quit(); // تقدري تشيلي الكومنت عنها بعدين لما تتأكدي إن التست كله بيقفل صح
    }
}

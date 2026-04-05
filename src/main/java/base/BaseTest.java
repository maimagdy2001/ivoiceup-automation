package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

    public class BaseTest {

        protected WebDriver driver;
        protected WebDriverWait wait;

        @BeforeMethod
        public void setup() {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new"); // مهم للـ pipeline
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        }

        @AfterMethod
        public void teardown() {
            driver.quit();
        }
    }


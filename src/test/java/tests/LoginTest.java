package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.MailpitOTPService;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginWithOtp() throws Exception {

        driver.get("https://development.ivoiceup.com/login");

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("mai.magdy.abdeleal@gmail.com", "Mai202000@");

        MailpitOTPService mailpit = new MailpitOTPService(
                "https://mailpit-bg840gwkg4co0so0oo4s00so.cool.ivoiceup.com"
        );

        String otp = mailpit.getOtp(10, 1);

        loginPage.enterOtp(otp);
    }
}
package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.FormBuilderPage;
import pages.LoginPage;
import utils.MailpitOTPService;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginWithOtp() throws Exception {
        driver.get("https://development.ivoiceup.com/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("mai.magdy.abdeleal@gmail.com", "Mai202002223@");

        // 1. هنشوف إحنا في أنهي صفحة دلوقتي عن طريق الـ URL الحالي
        String currentUrl = driver.getCurrentUrl();

        // 2. لو الـ URL لسه فيه كلمة login أو auth/otp (يعني طالب OTP فعلاً)
        if (currentUrl.contains("otp") || currentUrl.contains("login")) {
            try {
                // هنجيب الـ OTP ونكتبه من غير أي \n زيادة
                MailpitOTPService mailpit = new MailpitOTPService("https://mailpit-development.cool.ivoiceup.com/");
                String otp = mailpit.getOtp(10, 1);
                loginPage.enterOtp(otp);
            } catch (Exception e) {
                System.out.println("صفحة الـ OTP ظهرت بس حصلت مشكلة في قرايته: " + e.getMessage());
            }
        } else {
            // 3. لو الـ URL اتغير ودخل علطول على صفحة الحسابات
            System.out.println("السيستم مطلعش صفحة الـ OTP ودخل علطول على صفحة اختيار الحساب.");
        }

        // 4. في كلتا الحالتين.. بنختار الـ Organization ونعمل Submit
        loginPage.selectAccountAndSubmit("Your Organization");
        // 1. خليه يروح لصفحة الـ Form Builder علطول بعد اللوجن
        driver.get("https://development.ivoiceup.com/portal/admin/form-builder/complaint-form");

        // 2. نادي كلاس الصفحة الجديدة واقفل الـ Toggle واعملي Save
        FormBuilderPage formBuilderPage = new FormBuilderPage(driver);
        formBuilderPage.disableContactInfo();
        formBuilderPage.clickSave();
    }

}
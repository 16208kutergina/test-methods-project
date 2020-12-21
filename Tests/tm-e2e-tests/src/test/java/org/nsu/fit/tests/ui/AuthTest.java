package org.nsu.fit.tests.ui;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.shared.Screen;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AuthTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Login as admin.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login feature")
    public void loginAdminTest() {
        new LoginScreen(browser)
                .loginAsAdmin();
    }

    @Test(description = "Login as customer.", dependsOnMethods = "loginAdminTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login feature")
    public void loginCustomerTest() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        AdminScreen screen = (AdminScreen) new AdminScreen(browser)
                .createCustomer()
                .fillEmail(person.getEmail())
                .fillPassword(person.getPassword())
                .fillFirstName(person.getFirstName())
                .fillLastName(person.getLastName())
                .clickSubmit();

        screen.logout()
                .loginAsCustomer(person.getEmail(), person.getPassword());

    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}

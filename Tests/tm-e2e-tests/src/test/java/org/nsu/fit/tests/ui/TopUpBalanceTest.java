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
import org.nsu.fit.tests.ui.screen.CustomerScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;

public class TopUpBalanceTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Top up balance via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Top up balance feature")
    public void topUpBalanceTest() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        AdminScreen screen = (AdminScreen) new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(person.getEmail())
                .fillPassword(person.getPassword())
                .fillFirstName(person.getFirstName())
                .fillLastName(person.getLastName())
                .clickSubmit();

        CustomerScreen customerScreen = (CustomerScreen) screen.logout()
                .loginAsCustomer(person.getEmail(), person.getPassword())
                .topUpBalance()
                .fillMoney(10)
                .clickSubmit();

        assertEquals(10, customerScreen.getBalance());

    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }

}

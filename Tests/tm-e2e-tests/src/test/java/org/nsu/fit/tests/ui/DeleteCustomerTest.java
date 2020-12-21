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
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class DeleteCustomerTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Delete customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Delete customer feature")
    public void deleteCustomer() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        AdminScreen adminScreen = (AdminScreen) new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(person.getEmail())
                .fillPassword(person.getPassword())
                .fillFirstName(person.getFirstName())
                .fillLastName(person.getLastName())
                .clickSubmit();

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.login = person.getEmail();
        customerPojo.firstName = person.getFirstName();
        customerPojo.lastName = person.getLastName();

        int customerIndex = browser.getCustomerIndex(customerPojo);
        assertNotEquals(-1, customerIndex);

        adminScreen.deleteCustomer(customerIndex);
        long timeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - timeMillis < 3000){
        }
        customerIndex = browser.getCustomerIndex(customerPojo);
        assertEquals(-1, customerIndex);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}

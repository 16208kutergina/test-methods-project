package org.nsu.fit.tests.ui;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class CreateCustomerTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        new LoginScreen(browser)
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

        assertNotEquals(-1, browser.getCustomerIndex(customerPojo));

        // Лабораторная 4: Проверить что customer создан с ранее переданными полями.
        // Решить проблему с генерацией случайных данных.
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}

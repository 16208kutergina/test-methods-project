package org.nsu.fit.tests.ui.screen;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

import java.util.List;

public class AdminScreen extends Screen {

    public AdminScreen(Browser browser) {
        super(browser);
    }

    public CreateCustomerScreen createCustomer() {
        browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
        browser.click(By.xpath("//button[@title = 'Add Customer']"));

        return new CreateCustomerScreen(browser);
    }

    public CreatePlanScreen createPlan() {
        browser.click(By.xpath("//button[@title = 'Add plan']"));

        return new CreatePlanScreen(browser);
    }

    public void deletePlan(int planIndex) {
        String pathToTable = "//*[@id=\"root\"]/div/div/div/div/div[2]/div[2]/div/div/div/table/tbody/";
        browser.click(By.xpath(pathToTable + "tr[" + (planIndex+1) + "]/td[1]/div/button"));
        browser.waitForElement(By.xpath(pathToTable + "tr[" + (planIndex+1) + "]/td[1]/div/button"));
        browser.click(By.xpath(pathToTable + "tr[" + (planIndex+1) + "]/td[1]/div/button"));
    }

    public AdminScreen deleteCustomer(int indexInCustomerTable) {
        String pathToTable = "//*[@id=\"root\"]/div/div/div/div/div[1]/div[2]/div/div/div/table/tbody/";
        browser.click(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        browser.waitForElement(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        browser.click(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        return new AdminScreen(browser);
    }

    public LoginScreen logout() {
        browser.click(By.xpath("//*[@id=\"root\"]/div/div/div/div/p/a"));
        return new LoginScreen(browser);
    }
}

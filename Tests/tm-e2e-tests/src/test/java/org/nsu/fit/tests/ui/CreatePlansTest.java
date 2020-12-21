package org.nsu.fit.tests.ui;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.BaseProducer;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.text.TextProducer;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;

public class CreatePlansTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan feature")
    public void createPlan() {
        Fairy fairy = Fairy.create();
        BaseProducer baseProducer = fairy.baseProducer();
        TextProducer textProducer = fairy.textProducer();

        PlanPojo planPojo = new PlanPojo();
        planPojo.name = textProducer.word();
        planPojo.details = textProducer.sentence();
        planPojo.fee = baseProducer.randomInt(100);

    new LoginScreen(browser)
                .loginAsAdmin()
                .createPlan()
                .fillName(planPojo.name)
                .fillDetails(planPojo.details)
                .fillFee(planPojo.fee)
                .clickSubmit();


        assertNotEquals(-1, browser.getPlanIndex(planPojo));

    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}

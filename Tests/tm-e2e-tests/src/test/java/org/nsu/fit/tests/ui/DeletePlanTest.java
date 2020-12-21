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
import org.nsu.fit.services.rest.data.*;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;

public class DeletePlanTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Delete plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Delete plan feature")
    public void deletePlan() {
        Fairy fairy = Fairy.create();
        BaseProducer baseProducer = fairy.baseProducer();
        TextProducer textProducer = fairy.textProducer();

        PlanPojo planPojo = new PlanPojo();
        planPojo.name = textProducer.word();
        planPojo.details = textProducer.sentence();
        planPojo.fee = baseProducer.randomInt(100);

        AdminScreen adminScreen = (AdminScreen) new LoginScreen(browser)
                .loginAsAdmin()
                .createPlan()
                .fillName(planPojo.name)
                .fillDetails(planPojo.details)
                .fillFee(planPojo.fee)
                .clickSubmit();

        int planIndex = browser.getPlanIndex(planPojo);
        assertNotEquals(-1, planIndex);

        adminScreen.deletePlan(planIndex);
        long timeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - timeMillis < 3000){
        }
        planIndex = browser.getPlanIndex(planPojo);
        assertEquals(-1, planIndex);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}

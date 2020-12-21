package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.testng.annotations.Test;

public class PlansTest {
    private AccountTokenPojo adminToken;
    private PlanPojo planPojo;
    private RestClient restClient;

    @Test(description = "Create plan.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Plans feature.")
    public void createPlanTest() {
        restClient = new RestClient();
        adminToken = restClient.authenticate("admin", "setup");

        PlanPojo newPlanPojo = new PlanPojo();
        newPlanPojo.details = "details";
        newPlanPojo.name = "name";
        newPlanPojo.fee = 5;

        planPojo = restClient.createPlan(newPlanPojo, adminToken);
    }

    @Test(description = "Delete plan.", dependsOnMethods = "createPlanTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Plans feature.")
    public void deletePlanTest() {
        restClient.deletePlan(planPojo, adminToken);

    }
}

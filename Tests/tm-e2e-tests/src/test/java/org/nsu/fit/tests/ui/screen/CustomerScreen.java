package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CustomerScreen extends Screen {
    public CustomerScreen(Browser browser) {
        super(browser);
    }

    public TopUpBalanceScreen topUpBalance() {
        browser.click(By.xpath("//*[@id=\"root\"]/div/div/div/div/p[1]/a"));
        return new TopUpBalanceScreen(browser);
    }

    public int getBalance() {
       return Integer.parseInt(browser.getTextByElement("//*[@id=\"root\"]/div/div/div/div/h3").split(":")[1].trim());
    }
}

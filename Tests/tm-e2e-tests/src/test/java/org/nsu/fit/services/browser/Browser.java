package org.nsu.fit.services.browser;

import io.qameta.allure.Attachment;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.Closeable;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Please read: https://github.com/SeleniumHQ/selenium/wiki/Grid2
 */
public class Browser implements Closeable {
    private WebDriver webDriver;
    private String pathToTable;

    public Browser() {
        // create web driver.
        try {
            ChromeOptions chromeOptions = new ChromeOptions();

            // for running in Docker container as 'root'.
            chromeOptions.addArguments("no-sandbox");
            chromeOptions.addArguments("disable-dev-shm-usage");
            chromeOptions.addArguments("disable-setuid-sandbox");
            chromeOptions.addArguments("disable-infobars");

            chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

            // we use Windows platform for development only and not for AT launch.
            // For launch AT regression, we use Linux platform.
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Лабораторная 4: Указать путь до chromedriver на вашей системе.
                // Для того чтобы подобрать нужный chromedriver, необходимо посмотреть версию браузера Chrome
                // на системе, на которой будут запускаться тесты и скачать соотвествующий ей chromedriver с сайта:
                // https://chromedriver.chromium.org/downloads
                System.setProperty("webdriver.chrome.driver", "C:/Tools/chromedriver/chromedriver.exe");
                chromeOptions.setHeadless(Boolean.parseBoolean(System.getProperty("headless")));
                webDriver = new ChromeDriver(chromeOptions);
            } else {
                File f = new File("/usr/bin/chromedriver");
                if (f.exists()) {
                    chromeOptions.addArguments("single-process");
                    chromeOptions.addArguments("headless");
                    System.setProperty("webdriver.chrome.driver", f.getPath());
                    webDriver = new ChromeDriver(chromeOptions);
                }
            }

            if (webDriver == null) {
                throw new RuntimeException();
            }

            webDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Browser openPage(String url) {
        webDriver.get(url);
        return this;
    }

    public Browser waitForElement(By element) {
        return waitForElement(element, 10);
    }

    public Browser waitForElement(By element, int timeoutSec) {
        makeScreenshot();
        WebDriverWait wait = new WebDriverWait(webDriver, timeoutSec);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        makeScreenshot();
        return this;
    }

    public Browser click(By element) {
        makeScreenshot();
        webDriver.findElement(element).click();
        return this;
    }

    public Browser typeText(By element, String text) {
        makeScreenshot();
        webDriver.findElement(element).sendKeys(text);
        return this;
    }

    public String getValue(By element) {
        makeScreenshot();
        return webDriver.findElement(element).getAttribute("value");
    }

    public boolean isElementPresent(By element) {
        makeScreenshot();
        return webDriver.findElements(element).size() != 0;
    }

    public int getCustomerIndex(CustomerPojo customerPojo) {
        pathToTable = "//*[@id=\"root\"]/div/div/div/div/div[1]/div[2]/div/div/div/table/tbody/";
        while (true) {
            List<WebElement> logins = webDriver.findElements(By.xpath(pathToTable + "tr/td[2]"));
            List<WebElement> firstName = webDriver.findElements(By.xpath(pathToTable + "tr/td[3]"));
            List<WebElement> lastName = webDriver.findElements(By.xpath(pathToTable + "tr/td[4]"));
            WebElement button = webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/div/div[1]/table/tfoot/tr/td/div/div[3]/span[4]/button"));
            for (int i = 0; i < logins.size(); i++) {
                if (customerPojo.login.equals(logins.get(i).getText())
                        &&
                        customerPojo.firstName.equals(firstName.get(i).getText())
                        &&
                        customerPojo.lastName.equals(lastName.get(i).getText())
                ) {
                    System.out.println("aaaaa " + i);
                    return i;
                }
            }
            if (!button.isEnabled()) {
                break;
            } else {
                button.click();
            }
        }
        return -1;
    }

    public int getPlanIndex(PlanPojo planPojo) {
        pathToTable = "//*[@id=\"root\"]/div/div/div/div/div[2]/div[2]/div/div/div/table/tbody/";
        while (true) {
            List<WebElement> names = webDriver.findElements(By.xpath(pathToTable + "tr/td[2]"));
            List<WebElement> details = webDriver.findElements(By.xpath(pathToTable + "tr/td[3]"));
            List<WebElement> fees = webDriver.findElements(By.xpath(pathToTable + "tr/td[4]"));
            WebElement button = webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/div/div[2]/table/tfoot/tr/td/div/div[3]/span[4]/button"));
            for (int i = 0; i < names.size(); i++) {
                if (planPojo.name.equals(names.get(i).getText())
                        &&
                        planPojo.details.equals(details.get(i).getText())
                        &&
                        planPojo.fee == Integer.parseInt(fees.get(i).getText())
                ) {
                    return i;
                }
            }
            if (!button.isEnabled()) {
                break;
            } else {
                button.click();
            }
        }
        return -1;
    }

    public String getTextByElement(String xpath) {
        return webDriver.findElement(By.xpath(xpath)).getText();
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] makeScreenshot() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void close() {
        webDriver.close();
    }
}

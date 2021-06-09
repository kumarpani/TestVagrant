package driverManager;

import models.config.SiteConfig;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebDriver;
import parsers.ResourcesParser;

import java.time.Duration;

import static constants.SystemProperties.BROWSER;
import static constants.SystemProperties.ENV;

public class DriverManager {

  private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

  public WebDriver createDriver() {
    String runMode = System.getProperty("runMode", "local").toLowerCase();
    return runMode.equals("remote") ? createRemoteDriver() : createLocalDriver();
  }

  // TODO:: To be implemented
  private WebDriver createRemoteDriver() {
    throw new UnsupportedOperationException("Remote Execution not implemented");
  }

  private WebDriver createLocalDriver() {
    WebDriver webDriver =
        BROWSER.toLowerCase().contains("firefox")
            ? new FirefoxDriverManager().createDriver()
            : new ChromeDriverManager().createDriver();

    driverThreadLocal.set(webDriver);
    launch(driverThreadLocal.get());
    driverThreadLocal.get().manage().window().maximize();
    return driverThreadLocal.get();
  }

  private void launch(WebDriver webDriver) {
    String configPath = String.format("applicationConfig/%s/site_config.json", ENV);

    SiteConfig siteConfig = new ResourcesParser().getResource(configPath, SiteConfig.class);

    webDriver.get(siteConfig.getUrl());
    for (int retry = 0; retry < 3; retry++) {
      try {
        Awaitility.await()
            .atMost(Duration.ofMinutes(1))
            .until(
                () ->
                    webDriver
                        .getTitle()
                        .toLowerCase()
                        .contains(siteConfig.getTitle().toLowerCase()));
        break;
      } catch (Exception e) {
        webDriver.navigate().refresh();
      }
    }
  }
}

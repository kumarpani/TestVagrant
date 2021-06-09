package driverManager;

import io.github.bonigarcia.wdm.WebDriverManager;
import models.config.BrowserConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import parsers.ResourcesParser;

import java.util.Map;

import static constants.SystemProperties.HEADLESS;

public class FirefoxDriverManager {

  public WebDriver createDriver() {
    WebDriverManager.firefoxdriver().setup();
    return new FirefoxDriver(buildFirefoxOptions());
  }

  private FirefoxOptions buildFirefoxOptions() {
    String configPath = "browserConfig/firefox.json";

    BrowserConfig firefoxConfig =
        new ResourcesParser().getResource(configPath, BrowserConfig.class);

    Map<String, Object> capabilities = firefoxConfig.getDesiredCapabilities();
    Map<String, Object> preferences = firefoxConfig.getPreferences();

    FirefoxOptions options = new FirefoxOptions();
    FirefoxProfile profile = new FirefoxProfile();

    preferences.forEach((preference, value) -> profile.setPreference(preference, (String) value));
    capabilities.forEach(options::setCapability);
    options.setProfile(profile);

    if (HEADLESS) {
      options.addArguments("--headless");
    }

    return options;
  }
}

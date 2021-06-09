package driverManager;

import constants.SystemProperties;
import io.github.bonigarcia.wdm.WebDriverManager;
import models.config.BrowserConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import parsers.ResourcesParser;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static constants.SystemProperties.HEADLESS;

public class ChromeDriverManager {

  public WebDriver createDriver() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver(buildChromeOptions());
  }

  private ChromeOptions buildChromeOptions() {
    String configPath = "browserConfig/chrome.json";

    BrowserConfig chromeConfig = new ResourcesParser().getResource(configPath, BrowserConfig.class);

    List<String> arguments = chromeConfig.getArguments();
    List<String> extensions = chromeConfig.getExtensions();
    Map<String, Object> preferences = chromeConfig.getPreferences();
    Map<String, Object> experimentalOptions = chromeConfig.getExperimentalOptions();
    Map<String, Object> desiredCapabilities = chromeConfig.getDesiredCapabilities();

    ChromeOptions options = new ChromeOptions();

    if (desiredCapabilities.entrySet().size() > 0) {
      options.merge(new DesiredCapabilities(desiredCapabilities));
    }

    if (arguments.size() > 0) options.addArguments(arguments);

    if (HEADLESS) {
      arguments.add("--headless");
    }

    if (extensions.size() > 0) {
      extensions.forEach(extensionPath -> options.addExtensions(new File(extensionPath)));
    }

    if (preferences.keySet().size() > 0) {
      if (preferences.containsKey("download.default_directory")) {
        String downloadPath = Paths.get(SystemProperties.USER_DIR, "build/downloads").toString();
        File downloadDirectory = new File(downloadPath);
        if (!downloadDirectory.exists()) {
          downloadDirectory.mkdir();
        }
        preferences.put("download.default_directory", downloadPath);
      }
      options.setExperimentalOption("prefs", preferences);
    }

    if (experimentalOptions.keySet().size() > 0) {
      experimentalOptions.forEach(options::setExperimentalOption);
    }

    return options;
  }
}

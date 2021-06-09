package core;

import com.google.inject.Inject;
import org.awaitility.Awaitility;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.function.Supplier;

public class BrowserDriver {

  private final WebDriver driver;

  @Inject
  public BrowserDriver(WebDriver driver) {
    this.driver = driver;
  }

  public void navigateToUrl(String url) {
    driver.get(url);
  }

  public void waitUntil(Supplier<Boolean> condition) {
    waitUntil(condition, 30);
  }

  public void waitUntil(Supplier<Boolean> condition, long timeOutInSeconds) {
    try {
      Awaitility.await()
          .ignoreExceptions()
          .timeout(Duration.ofSeconds(timeOutInSeconds))
          .until(condition::get);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public void waitForPageToBeReady() {
    waitUntil(() -> this.jsDriver().executeScript("return document.readyState").equals("complete"));
  }

  private JavascriptExecutor jsDriver() {
    return ((JavascriptExecutor) this.driver);
  }
}

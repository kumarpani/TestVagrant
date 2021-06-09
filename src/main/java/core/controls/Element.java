package core.controls;

import com.google.inject.Inject;
import core.QueryFunctions;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class Element extends QueryFunctions {
  protected final WebDriver driver;
  private final ConditionFactory wait;
  protected By locator;
  private final Duration timeout;
  private final Actions actions;

  @Inject
  public Element(WebDriver driver, By locator) {
    this.driver = driver;
    this.locator = locator;
    actions = new Actions(driver);
    this.timeout = Duration.ofSeconds(10);
    this.wait = buildFluentWait(timeout);
  }

  public String getTextValue() {
    return getElement().getText().trim();
  }

  public String getAttributeValue(String attribute) {
    return getElement().getAttribute(attribute);
  }

  public Element click() {
    waitUntilPresent();
    wait.until(
        () -> {
          getElement().click();
          return true;
        });

    return this;
  }

  public boolean hasAttribute(String attribute) {
    String value = getElement().getAttribute(attribute);
    return value != null;
  }

  public Element dragDrop(Element destination) {
    actions.dragAndDrop(getElement(), destination.getElement()).build().perform();
    return this;
  }

  public boolean isPresent() {
    try {
      waitUntilCondition(ExpectedConditions.presenceOfElementLocated(locator));
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public boolean isPresent(Duration duration) {
    try {
      waitUntilCondition(ExpectedConditions.presenceOfElementLocated(locator), duration);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public Element waitUntilDisplayed() {
    try {
      waitUntilCondition(ExpectedConditions.visibilityOfElementLocated(locator));
      return this;
    } catch (Exception ex) {
      throw new RuntimeException(
          String.format("Error waiting for element with selector: %s to be displayed.", locator));
    }
  }

  public Element waitUntilDisplayed(Duration duration) {
    try {
      waitUntilCondition(ExpectedConditions.visibilityOfElementLocated(locator), duration);
      return this;
    } catch (Exception ex) {
      throw new RuntimeException(
          String.format("Error waiting for element with selector: %s to be displayed.", locator));
    }
  }

  public Element waitUntilPresent() {
    try {
      waitUntilCondition(ExpectedConditions.presenceOfElementLocated(locator));
      return this;
    } catch (Exception ex) {
      throw new RuntimeException(
          String.format("Error waiting for element presence with selector: %s.", locator));
    }
  }

  public Element scrollIntoView() {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getElement());
    return this;
  }

  protected WebElement getElement() {
    try {
      wait.atMost(Duration.ofSeconds(5)).until(() -> driver.findElement(locator) != null);
      return driver.findElement(locator);
    } catch (Exception ex) {
      throw new RuntimeException(String.format("Element with selector: %s not found", locator));
    }
  }

  protected WebElement find(By selector) {
    try {
      wait.atMost(Duration.ofSeconds(5)).until(() -> getElement().findElement(selector) != null);
      return getElement().findElement(selector);
    } catch (Exception ex) {
      throw new RuntimeException(String.format("Element with selector: %s not found", locator));
    }
  }

  private ConditionFactory buildFluentWait(Duration duration) {
    return Awaitility.await().atMost(duration).ignoreExceptions();
  }

  private <T> void waitUntilCondition(ExpectedCondition<T> webElementExpectedCondition) {
    waitUntilCondition(webElementExpectedCondition, timeout);
  }

  private <T> void waitUntilCondition(
      ExpectedCondition<T> webElementExpectedCondition, Duration duration) {
    wait.atMost(duration)
        .until(
            () -> {
              Object result = webElementExpectedCondition.apply(driver);
              return result != null
                      && result.getClass().getTypeName().toLowerCase().contains("boolean")
                  ? (boolean) result
                  : result != null;
            });
  }
}

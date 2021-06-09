package core;

import core.controls.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class BasePage extends QueryFunctions {
  protected WebDriver driver;

  public BasePage(WebDriver driver) {
    this.driver = driver;
  }

  protected Element element(By locator) {
    return new Element(driver, locator);
  }

  protected TextBox textbox(By locator) {
    return new TextBox(driver, locator);
  }

  protected ElementCollection elementCollection(By locator) {
    return new ElementCollection(driver, locator);
  }

  protected LeftPanelMenu leftMenu(String menu) {
    return new LeftPanelMenu(driver, menu);
  }

  protected Grid grid(String idSelector) {
    return new Grid(driver, idSelector);
  }

  protected Grid grid(By locator) {
    return new Grid(driver, locator);
  }

  protected Dropdown dropdown(By locator) {
    return new Dropdown(driver, locator);
  }

  protected SelectWithGroups selectWithGroups(By locator) {
    return new SelectWithGroups(driver, locator);
  }

  protected UnOrderedList unOrderedList(By locator) {
    return new UnOrderedList(driver, locator);
  }

  protected OrderedList orderedList(By locator) {
    return new OrderedList(driver, locator);
  }

  protected void waitForLoading() {
    try {
      elementCollection(query("[class*='ui-widget-overlay'][id*='processing' i]"))
          .waitUntilInVisible(Duration.ofSeconds(30));
    } catch (Exception ex) {
      throw new RuntimeException("Loading not complete." + ex.getMessage());
    }
  }

  protected <T> T createPageInstance(Class<T> tClass) {
    try {
      return tClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  protected By getTitleLocator(String title) {
    return query(String.format("//h1[contains(text() , '%s')]", title));
  }
}

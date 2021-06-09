package core.controls;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductionTile extends Element {

  @Inject
  public ProductionTile(WebDriver driver, String productionName) {
    super(driver, By.xpath(String.format("//li[.//a[text() = '%s']]", productionName)));
  }

  private final By dropDown = By.cssSelector("a[data-toggle='dropdown']");

  @Inject
  public ProductionTile(WebDriver driver, By locator) {
    super(driver, locator);
  }

  public void delete() {
    selectDropDown("Delete");
    Element okButton = new Element(driver, query("//button[contains(text(), 'OK')]"));
    okButton.click();

    Element deleteNotification =
        new Element(driver, query("//p[text() = 'Production deleted successfully']"));
    deleteNotification.waitUntilDisplayed();

    driver.navigate().refresh();
  }

  public void viewProduction() {
    find(query(".prod-Title")).click();
  }

  private void selectDropDown(String option) {
    find(dropDown).click();
    By optionSelector = query(String.format("//a[@data-toggle='tab' and text() = '%s']", option));
    new Element(driver, optionSelector).click();
  }
}

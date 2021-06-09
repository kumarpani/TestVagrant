package core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.time.Duration;

public class SelectWithGroups extends Element {

  public SelectWithGroups(WebDriver driver, By locator) {
    super(driver, locator);
  }

  public void selectOption(String group, String optionText) {
    ByChained selector =
        new ByChained(
            this.locator,
            query(String.format("[label*='%s' i]", group)),
            query(String.format("//option[text() = '%s']", optionText)));

    Element option = new Element(driver, selector);
    if (option.isPresent(Duration.ofSeconds(5))) {
      option.click();
      return;
    }

    throw new RuntimeException(optionText + " option not found");
  }
}

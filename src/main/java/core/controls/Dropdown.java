package core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class Dropdown extends Element {

  private final ElementCollection options;

  public Dropdown(WebDriver driver, By locator) {
    super(driver, locator);
    options = new ElementCollection(driver, new ByChained(locator, By.cssSelector("option")));
  }

  public List<String> getOptions() {
    options.waitUntilPresent();
    return options.getTextValues();
  }

  public void selectOption(String optionText) {
    ByChained selector =
        new ByChained(locator, query(String.format("//option[text() = '%s']", optionText)));

    Element option = new Element(driver, selector);
    if (option.isPresent(Duration.ofSeconds(5))) {
      new Select(getElement()).selectByVisibleText(optionText);
      return;
    }

    throw new RuntimeException(optionText + " option not found");
  }

  public boolean optionExist(String option) {
    return getOptions().parallelStream().anyMatch(name -> name.equalsIgnoreCase(option));
  }

}

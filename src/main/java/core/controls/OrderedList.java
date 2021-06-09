package core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderedList extends UnOrderedList {

  public OrderedList(WebDriver driver, By locator) {
    super(driver, locator);
  }
}

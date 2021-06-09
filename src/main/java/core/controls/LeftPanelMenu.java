package core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LeftPanelMenu extends Element {

  public LeftPanelMenu(WebDriver driver, String menuText) {
    super(driver, By.name(menuText));
  }
}

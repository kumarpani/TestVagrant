package core.controls;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TextBox extends Element {

  @Inject
  public TextBox(WebDriver driver, By locator) {
    super(driver, locator);
  }

  public void setText(CharSequence value) {
    super.getElement().sendKeys(value);
  }

}

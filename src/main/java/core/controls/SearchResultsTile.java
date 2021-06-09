package core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

public class SearchResultsTile extends Element {

  private String countSelector =
      "//a[@data-original-title='PLACEHOLDER']//*[contains(@id, 'countCount') or contains(@id ,'tilecount')]";

  public SearchResultsTile(WebDriver driver, String title) {
    super(driver, By.cssSelector(String.format("[data-original-title='%s' i]", title)));
    countSelector = this.countSelector.replace("PLACEHOLDER", title);
  }

  public String getCount() {
    By selector = new ByChained(locator, query(countSelector));
    return new Element(driver, selector)
        //
        .waitUntilDisplayed()
        .getTextValue();
  }

  public void addTileToResults() {
    Element addTileIcon = new Element(driver, new ByChained(locator, query("i[class*='addTile']")));
    addTileIcon.click();
  }
}

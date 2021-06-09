package core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.time.Duration;
import java.util.List;

public class UnOrderedList extends Element {

  public UnOrderedList(WebDriver driver, By locator) {
    super(driver, locator);
  }

  public List<String> getListItems() {
    ByChained listItemSelector = new ByChained(locator, By.cssSelector("li"));
    ElementCollection listItems = new ElementCollection(driver, listItemSelector);
    return listItems.waitUntilPresent().getTextValues();
  }

  public void clickListItemMatchingText(String text) {
    clickListItemMatchingText(text, false);
  }

  public void clickListItemMatchingText(String text, boolean scrollIntoView) {
    String listItemSelector =
        String.format("//li[text()='%1$s'] | //li[.//*[text() = '%1$s']]", text);
    By selector = new ByChained(locator, query(listItemSelector));
    Element listItem = new Element(driver, selector);
    if (listItem.isPresent(Duration.ofSeconds(10))) {
      if (scrollIntoView) listItem.scrollIntoView();
      listItem.click();
      return;
    }

    throw new RuntimeException(String.format("%s list-item not found", text));
  }

  public void clickIconInListItemMatchingText(String text) {
    clickIconInListItemMatchingText(text, false);
  }

  public void clickIconInListItemMatchingText(String text, boolean scrollIntoView) {
    String listItemSelector =
        String.format("(//li[text()='%1$s'] | //li[.//*[text() = '%1$s']])//i", text);
    By selector = new ByChained(locator, query(listItemSelector));
    Element listItem = new Element(driver, selector);
    if (listItem.isPresent(Duration.ofSeconds(5))) {
      if (scrollIntoView) listItem.scrollIntoView();
      listItem.click();
      return;
    }

    throw new RuntimeException(String.format("%s list-item not found", text));
  }

  public List<String> getTextOfListItemDescendants(By descendantLocator) {
    ByChained listItemSelector = new ByChained(locator, query("li"), descendantLocator);
    ElementCollection listItems = new ElementCollection(driver, listItemSelector);
    return listItems.waitUntilPresent().getAttributeValues("innerText");
  }

  public List<String> getAttributeOfListItemDescendants(By descendantLocator, String attribute) {
    ByChained listItemSelector = new ByChained(locator, query("li"), descendantLocator);
    ElementCollection listItems = new ElementCollection(driver, listItemSelector);
    return listItems.waitUntilPresent().getAttributeValues(attribute);
  }

  public Element getListItem(By selector) {
    return new Element(driver, new ByChained(locator, selector));
  }
}

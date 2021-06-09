package core;

import org.openqa.selenium.By;

public abstract class QueryFunctions {
  protected By query(String value) {
    return value.contains("//") ? By.xpath(value) : By.cssSelector(value);
  }

  protected By queryById(String id) {
    return By.id(id);
  }

  protected By queryByName(String name) {
    return By.name(name);
  }

  protected By queryByAttribute(String attribute, String value) {
    String selector =
        String.format(
            "//*[normalize-space(translate(@%1$s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')) = '%2$s']",
            attribute, value.toLowerCase());

    return query(selector);
  }

  protected By queryByText(String text) {
    String selector =
        String.format(
            "//*[normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')) = '%1$s']",
            text.toLowerCase());
    return query(selector);
  }

  protected By queryByText(String text, Boolean partial) {
    if (!partial) {
      return queryByText(text);
    }

    String selector =
        String.format(
            "//*[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')) , '%1$s')]",
            text.toLowerCase());

    return query(selector);
  }
}

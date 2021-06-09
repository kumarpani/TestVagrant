package pages.search;

import core.controls.Dropdown;
import models.search.MetaData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.util.List;

public class MetaDataDialog extends SearchPage {

  @Inject
  public MetaDataDialog(WebDriver driver) {
    super(driver);
  }

  private final By fieldSelect = queryById("metatagSelect");
  private final By metaDataValueTextBox = queryById("val1");
  private final By insertIntoQueryButton = queryById("insertQueryBtn");

  public List<String> getMetaDataFields() {
    Dropdown dropdown = dropdown(fieldSelect);
    dropdown.waitUntilDisplayed();
    return dropdown.getOptions();
  }

  public SearchPage insertMetaData(MetaData metaData) {
    dropdown(fieldSelect).selectOption(metaData.getField());
    textbox(metaDataValueTextBox).setText(metaData.getValue());
    element(insertIntoQueryButton).click();
    waitForLoading();
    return createPageInstance(SearchPage.class);
  }
}

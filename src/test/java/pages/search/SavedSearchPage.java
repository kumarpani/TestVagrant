package pages.search;

import com.google.inject.Inject;
import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class SavedSearchPage extends BasePage {

  @Inject
  public SavedSearchPage(WebDriver driver) {
    super(driver);
  }

  private final By savedSearchGrid =
      query("div[class= 'dataTables_scrollBody'] table[id='SavedSearchGrid']");
  private final By editButton = queryById("rbnEdit");
  private final By exportButton = queryById("rbnExport");
  private final By deleteButton = queryById("rbnDelete");
  private final By okButton = query("//button[contains(text() , 'Ok')]");

  public SearchPage editSavedSearch(Map<String, String> searchCriteria) {
    selectRow(searchCriteria);
    element(editButton).click();
    waitForLoading();
    return createPageInstance(SearchPage.class);
  }

  public SearchPage deleteSavedSearch(Map<String, String> searchCriteria) {
    selectRow(searchCriteria);
    element(deleteButton).click();
    element(okButton).click();
    waitForLoading();
    return createPageInstance(SearchPage.class);
  }

  public String getColumnValue(String columnName, Map<String, String> searchCriteria) {
    return grid(savedSearchGrid).getColumnValue(columnName, searchCriteria);
  }

  public boolean isSavedSearchPresent(Map<String, String> searchCriteria) {
    return grid(savedSearchGrid).rowPresent(searchCriteria);
  }

  public ExportDialog export(Map<String, String> searchCriteria) {
    selectRow(searchCriteria);
    element(exportButton).click();
    waitForLoading();
    return createPageInstance(ExportDialog.class);
  }

  public SavedSearchPage selectRow(Map<String, String> searchCriteria) {
    grid(savedSearchGrid).selectRow(searchCriteria);
    return this;
  }
}

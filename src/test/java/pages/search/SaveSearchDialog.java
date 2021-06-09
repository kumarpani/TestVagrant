package pages.search;

import com.google.inject.Inject;
import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

public class SaveSearchDialog extends BasePage {

  @Inject
  public SaveSearchDialog(WebDriver driver) {
    super(driver);
  }

  private final By saveAsNewSearchRadioButton = query("[id='saveAsNewSearchRadioButton'] + i");
  private final By searchNameTextbox = queryById("txtSaveSearchName");
  private final By saveButton = queryById("btnEdit");
  private final By dialog = queryByAttribute("aria-describedby", "divSaveSearch");

  // My Saved Search tree
  private final By saveAsNewSearchTree = query("#treeAndInput [class='tree-wrapper']");
  private final By mySavedSearchTreeItem =
      new ByChained(saveAsNewSearchTree, query("li:nth-of-type(1)"));

  public <T> T saveAsNewMySavedSearch(String searchName, Class<T> tClass) {
    element(dialog).waitUntilDisplayed();
    element(saveAsNewSearchRadioButton).click();
    element(mySavedSearchTreeItem).click();
    textbox(searchNameTextbox).setText(searchName);
    element(saveButton).click();
    waitForLoading();
    return createPageInstance(tClass);
  }
}

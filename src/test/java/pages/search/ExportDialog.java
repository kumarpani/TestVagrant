package pages.search;

import com.google.inject.Inject;
import core.BasePage;
import core.controls.UnOrderedList;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ExportDialog extends BasePage {

  @Inject
  public ExportDialog(WebDriver driver) {
    super(driver);
  }

  private final By title = query("//h2[text() = 'Select Fields & Data to Export']");
  private final By metaDataUnorderedList = queryById("tab1-export");
  private final By addToSelectedButton = queryById("addFormObjects-coreList");
  private final By runReportButton = queryById("btnRunReport");
  private final By selectedForExportOrderedList = queryById("coreList");
  private final By selectedForExportMetaDataContainer = query(".itemFriendlyName");

  public <T> T addMetaDataFieldsAndRunReport(List<String> metaDataFields, Class<T> tClass) {
    element(title).waitUntilDisplayed(Duration.ofSeconds(15));
    UnOrderedList metaDataUnOrderedList = unOrderedList(metaDataUnorderedList);
    metaDataFields.forEach(
        field -> metaDataUnOrderedList.clickIconInListItemMatchingText(field, true));
    element(addToSelectedButton).scrollIntoView().waitUntilDisplayed().click();

    List<String> differences =
        getSelectedForExportFields().stream()
            .filter(element -> !metaDataFields.contains(element))
            .collect(Collectors.toList());

    if (differences.size() > 0) {
      throw new RuntimeException(StringUtils.join(differences) + " not added for export");
    }

    element(runReportButton).scrollIntoView().click();
    return createPageInstance(tClass);
  }

  public List<String> getSelectedForExportFields() {
    return orderedList(selectedForExportOrderedList)
        .getTextOfListItemDescendants(selectedForExportMetaDataContainer);
  }
}

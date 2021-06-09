package pages.reports;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

public class TallyReportPage extends BasePage {

  @Inject
  public TallyReportPage(WebDriver driver) {
    super(driver);
  }

  // Source Selection
  private final By selectedSourceButton = queryById("select-source");
  private final By securityGroups = query("//a[.//*[text() = 'Security Groups']]");
  private final By securityGroupUnorderedList = query("ul[id='secgroup']");
  private final By saveSecGroupSelection = query("button[id='secgroup']");

  // Metadata
  private final By metadataButton = queryById("select-source1");
  private final By metaDataSelect = queryById("metadataselect");
  private final By metaDataPopupCloseButton = query("#metadataPopup [id='close']");

  private final By tallyApplyButton = queryById("btnTallyApply");
  private final By tallyAllButton = queryById("btnTallyAll");
  private final By tallyActionButton = queryById("tallyactionbtn");
  private final By actionsUnorderedList = query("#tallyactionbtn + ul");
  private final By recordsSavedNotification = queryByText("Records saved successfully");

  public TallyReportPage selectSecurityGroupSource(String securityGroup) {
    element(selectedSourceButton).click();
    element(securityGroups).click();
    unOrderedList(securityGroupUnorderedList).clickIconInListItemMatchingText(securityGroup);
    element(saveSecGroupSelection).click();
    return this;
  }

  public TallyReportPage selectMetaData(String metaData) {
    element(metadataButton).click();
    dropdown(metaDataSelect).selectOption(metaData);
    element(metaDataPopupCloseButton).click();
    return this;
  }

  public TallyReportPage applyTally() {
    element(tallyApplyButton).click();
    waitForLoading();
    return this;
  }

  public TallyReportPage bulkReleaseAllRecords(String securityGroup) {
    element(tallyAllButton).click();
    element(tallyActionButton).click();
    unOrderedList(actionsUnorderedList).clickListItemMatchingText("Bulk Release");
    waitForLoading();
    createPageInstance(BulkReleaseDialog.class).selectSecurityGroupAndRelease(securityGroup);
    element(recordsSavedNotification).click();
    return this;
  }
}

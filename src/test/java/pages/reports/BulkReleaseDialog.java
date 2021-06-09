package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

public class BulkReleaseDialog extends TallyReportPage {

  @Inject
  public BulkReleaseDialog(WebDriver driver) {
    super(driver);
  }

  private final By releaseButton = queryById("btnRelease");
  private final By finalizeReleaseButton = queryById("btnfinalizeAssignment");

  public void selectSecurityGroupAndRelease(String securityGroup) {
    By releaseSecurityGroupCheckbox =
        query(
            String.format(
                "//form[@id='Edit User Group']//*[text() = '%s']//preceding-sibling::*//i",
                securityGroup));
    element(releaseSecurityGroupCheckbox).click();
    element(releaseButton).click();
    element(finalizeReleaseButton).click();
  }
}

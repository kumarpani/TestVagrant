package pages;

import models.ImpersonationDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static constants.ImpersonateTo.PROJECT_ADMINISTRATOR;
import static constants.ImpersonateTo.REVIEW_MANAGER;

public class ImpersonationDialog extends HomePage {

  @Inject
  public ImpersonationDialog(WebDriver driver) {
    super(driver);
  }

  private final By dialog = query("[aria-describedby='ChangeRolePopup']");
  private final By impersonateToSelect = queryById("ddlAvailableRoles");
  private final By domainSelect = queryById("ddlAvailableDomains");
  private final By projectSelect = queryById("ddlAvailableProjects");
  private final By securityGroupSelect = queryById("ddlAvailableSecGroup");
  private final By saveButton = query("[value='Save']");

  public HomePage impersonateAs(ImpersonationDetails details) {
    HomePage page = createPageInstance(HomePage.class);
    element(dialog).waitUntilDisplayed();

    dropdown(impersonateToSelect).selectOption(details.getImpersonateTo());

    if (!details.getImpersonateTo().equalsIgnoreCase(PROJECT_ADMINISTRATOR)) {
      dropdown(domainSelect).selectOption(details.getDomain());
      dropdown(projectSelect).selectOption(details.getProject());
      dropdown(securityGroupSelect).selectOption(details.getSecurityGroup());
    }

    element(saveButton).click();

    if (details.getImpersonateTo().equalsIgnoreCase(REVIEW_MANAGER)) {
      element(queryByText("Review Manager Dashboard for", true)).waitUntilDisplayed();
    }
    return page;
  }
}

package pages;

import com.google.inject.Inject;
import core.BasePage;
import core.controls.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.manage.ManageProjectFieldsPage;
import pages.manage.SecurityGroupPage;
import pages.productions.ProductionsAndExportsPage;
import pages.reports.ReportsPage;
import pages.search.SavedSearchPage;
import pages.search.SearchPage;

import static constants.LeftPanelMenu.*;
import static constants.manage.ManageSubMenu.PROJECT_FIELDS;
import static constants.manage.ManageSubMenu.SECURITY_GROUP;
import static constants.search.SearchSubMenu.SAVED;
import static constants.search.SearchSubMenu.SESSION;

public class HomePage extends BasePage {
  private final By notificationsButton = query("#activity");
  private final By userSelectorButton = queryById("user-selector");
  private final By userSelectorUnorderedList = query("#utility-group ul");

  @Inject
  public HomePage(WebDriver driver) {
    super(driver);
  }

  public SearchPage navigateToSessionSearch() {
    leftMenu(SEARCH).click();
    leftMenu(SESSION).click();
    element(getTitleLocator("Search")).waitUntilDisplayed();
    return createPageInstance(SearchPage.class);
  }

  public SavedSearchPage navigateToSavedSearch() {
    leftMenu(SEARCH).click();
    leftMenu(SAVED).click();
    element(getTitleLocator("Search")).waitUntilDisplayed();
    return createPageInstance(SavedSearchPage.class);
  }

  public ProductionsAndExportsPage navigateToProductions() {
    leftMenu(PRODUCTIONS).click();
    element(getTitleLocator("Productions & Exports")).waitUntilDisplayed();
    return createPageInstance(ProductionsAndExportsPage.class);
  }

  public SecurityGroupPage navigateToSecurityGroup() {
    leftMenu(MANAGE).click();
    leftMenu(SECURITY_GROUP).click();
    element(getTitleLocator("Security Groups")).waitUntilDisplayed();
    return createPageInstance(SecurityGroupPage.class);
  }

  public ManageProjectFieldsPage navigateToProjectFields() {
    leftMenu(MANAGE).click();
    leftMenu(PROJECT_FIELDS).click();
    element(getTitleLocator("Manage Project Fields")).waitUntilDisplayed();
    return createPageInstance(ManageProjectFieldsPage.class);
  }

  public ReportsPage navigateToReports() {
    leftMenu(REPORTS).click();
    element(getTitleLocator("Reports")).waitUntilDisplayed();
    return createPageInstance(ReportsPage.class);
  }

  public NotificationsSection expandNotifications() {
    Element notificationsIcon = element(notificationsButton);
    if (!notificationsIcon.getAttributeValue("class").contains("active")) {
      notificationsIcon.click();
    }
    waitForLoading();
    return createPageInstance(NotificationsSection.class);
  }

  public NotificationsSection collapseNotifications() {
    Element notificationsIcon = element(notificationsButton);
    if (notificationsIcon.getAttributeValue("class").contains("active")) {
      notificationsIcon.click();
    }
    waitForLoading();
    return createPageInstance(NotificationsSection.class);
  }

  public ImpersonationDialog changeRole() {
    element(userSelectorButton).click();
    unOrderedList(userSelectorUnorderedList).clickListItemMatchingText("Change Role");
    return createPageInstance(ImpersonationDialog.class);
  }
}

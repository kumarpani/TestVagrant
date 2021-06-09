package pages.manage;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

public class SecurityGroupPage extends BasePage {

  @Inject
  public SecurityGroupPage(WebDriver driver) {
    super(driver);
  }

  private final By createButton = queryById("btnNewSecurityGroup");
  private final By securityGroupSelect = queryById("ddlSecurityGroupsList");
  private final By securityGroupNameTextBox = queryById("txtSecurityGroupName");
  private final By saveButton = queryById("btnSaveSecurityGroup");
  private final By deleteSecurityGroupButton = queryById("btnDeleteSecurityGroup");
  private final By yesButton = query("//button[contains(text(), 'Yes')]");
  private final By successNotification = query("//p[text() = 'Security group added successfully']");

  public SecurityGroupPage createSecurityGroup(String name) {
    if (securityGroupExist(name)) {
      return this;
    }
    element(createButton).click();
    textbox(securityGroupNameTextBox).setText(name);
    element(saveButton).click();
    element(successNotification).click();
    return this;
  }

  public SecurityGroupPage createSecurityGroup(String name, boolean deleteIfExists) {
    if (securityGroupExist(name) && deleteIfExists) {
      dropdown(securityGroupSelect).selectOption(name);
      element(deleteSecurityGroupButton).click();
      element(yesButton).click();
      element(queryByText("Security group deleted successfully")).waitUntilDisplayed();
    }

    createSecurityGroup(name);
    return this;
  }

  public <T> T createSecurityGroup(String name, boolean deleteIfExists, Class<T> tClass) {
    if (securityGroupExist(name) && deleteIfExists) {
      dropdown(securityGroupSelect).selectOption(name);
      element(deleteSecurityGroupButton).click();
      element(yesButton).click();
      element(queryByText("Security group deleted successfully")).waitUntilDisplayed();
    }

    createSecurityGroup(name);
    return createPageInstance(tClass);
  }

  public boolean securityGroupExist(String securityGroup) {
    return dropdown(securityGroupSelect).optionExist(securityGroup);
  }
}

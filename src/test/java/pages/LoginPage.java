package pages;

import com.google.inject.Inject;
import core.BasePage;
import models.Credentials;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class LoginPage extends BasePage {

  private final By usernameTextBox = queryById("txtBxUserID");
  private final By passwordTextBox = queryById("txtBxUserPass");
  private final By loginButton = queryByName("submit");
  private final By projectSelector = queryById("project-selector");
  private final By activeSessionMessage = queryByText("Active Session");
  private final By yesButton = queryByText("Yes");
  private final By sessionExpiredNotification =
      queryByText("Your current session expired. Please log in again");
  private final By closeNotificationButton = queryById("botClose1");

  @Inject
  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public HomePage login(Credentials credentials) {
    textbox(usernameTextBox).setText(credentials.getUsername());
    textbox(passwordTextBox).setText(credentials.getPassword());
    element(loginButton).click();
    handleActiveSessionPopup(credentials);
    element(projectSelector).waitUntilDisplayed();
    return createPageInstance(HomePage.class);
  }

  private void handleActiveSessionPopup(Credentials credentials) {
    if (element(activeSessionMessage).isPresent(Duration.ofSeconds(3))) {
      element(yesButton).click();
      element(sessionExpiredNotification).waitUntilDisplayed();
      element(closeNotificationButton).click();
      login(credentials);
    }
  }
}

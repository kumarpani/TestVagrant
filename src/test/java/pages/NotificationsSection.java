package pages;

import com.google.inject.Inject;
import core.BasePage;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class NotificationsSection extends BasePage {

  @Inject
  public NotificationsSection(WebDriver driver) {
    super(driver);
  }

  private final By badge = query("b.badge");
  private final By notificationBodyUnorderedList = query(".notification-body");

  public <T> T clickNotification(String notificationText, Class<T> tClass) {
    unOrderedList(notificationBodyUnorderedList).clickListItemMatchingText(notificationText);
    waitForLoading();
    return createPageInstance(tClass);
  }

  public int getUnseenNotificationsCount() {
    return Integer.parseInt(element(badge).getTextValue());
  }

  public int getNotificationsCountFor(String notificationText) {
    return (int)
        unOrderedList(notificationBodyUnorderedList).getListItems().stream()
            .filter(notification -> notification.contains(notificationText))
            .count();
  }

  public NotificationsSection waitForNotificationCountToBeMoreThan(
      String notificationText, int count) {
    HomePage homePage = createPageInstance(HomePage.class);
    Awaitility.await()
        .atMost(Duration.ofSeconds(20))
        .await()
        .until(
            () -> {
              homePage.expandNotifications();
              boolean result = getNotificationsCountFor(notificationText) > count;
              homePage.collapseNotifications();
              driver.navigate().refresh();
              waitForLoading();
              return result;
            });

    homePage.expandNotifications();
    return this;
  }
}

package pages;

import com.google.inject.Inject;
import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DocViewPage extends BasePage {

  @Inject
  public DocViewPage(WebDriver driver) {
    super(driver);
  }

  private final By searchDataTable = query("table#SearchDataTable");
  private final By reviewModeText = queryByText("REVIEW MODE");

  public int getDocsCount() {
    waitForLoading();
    element(reviewModeText).waitUntilDisplayed();
    return grid(searchDataTable).getDataRowsCount();
  }
}

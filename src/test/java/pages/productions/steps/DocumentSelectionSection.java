package pages.productions.steps;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DocumentSelectionSection extends ProductionsPage {

  @Inject
  public DocumentSelectionSection(WebDriver driver) {
    super(driver);
  }

  private final By totalDocumentsCount = queryById("TotalDocumentsCount");

  public int getTotalDocumentsCount() {
    String totalDocuments = element(totalDocumentsCount).waitUntilDisplayed().getTextValue().trim();
    if (!totalDocuments.isEmpty()) {
      return Integer.parseInt(totalDocuments);
    }

    return 0;
  }
}

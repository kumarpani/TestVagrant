package pages.productions.steps;

import com.google.inject.Inject;
import models.production.ProductionSummary;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SummaryAndPreviewSection extends ProductionsPage {

  @Inject
  public SummaryAndPreviewSection(WebDriver driver) {
    super(driver);
  }

  public ProductionSummary getSummary() {
    String totalDocuments = element(getLabelValueLocator("Total Documents")).getTextValue();
    String totalPages = element(getLabelValueLocator("Total Pages")).getTextValue();
    String numberOfCustodians =
        element(getLabelValueLocator("Number Of Custodians")).getTextValue();

    return ProductionSummary.builder()
        .totalDocuments(parseInteger(totalDocuments))
        .totalPages(parseInteger(totalPages))
        .numberOfCustodians(parseInteger(numberOfCustodians))
            .build();
  }

  private int parseInteger(String value) {
    return value.isEmpty() || value.isBlank() ? 0 : Integer.parseInt(value);
  }

  private By getLabelValueLocator(String label) {
    String labelValueSelector =
        String.format("//label[contains(text() , '%s')]/following-sibling::label", label);
    return query(labelValueSelector);
  }
}

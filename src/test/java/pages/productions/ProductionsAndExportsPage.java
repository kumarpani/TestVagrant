package pages.productions;

import com.google.inject.Inject;
import core.BasePage;
import core.controls.ProductionTile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.productions.steps.ProductionsPage;

import java.time.Duration;

public class ProductionsAndExportsPage extends BasePage {

  @Inject
  public ProductionsAndExportsPage(WebDriver driver) {
    super(driver);
  }

  private final By addProductionButton = queryByText("Add a New Production");
  private final By productionTitle = query("//h1[contains(text() , 'Production')]");

  public ProductionsPage navigateToAddProductionPage() {
    element(addProductionButton).click();
    element(productionTitle).waitUntilDisplayed();
    return createPageInstance(ProductionsPage.class);
  }

  public ProductionsAndExportsPage deleteProduction(String productionName) {
    productionTile(productionName).delete();
    return this;
  }

  public boolean isProductionAvailable(String productionName) {
    return productionTile(productionName).isPresent(Duration.ofSeconds(5));
  }

  public ProductionsPage viewProduction(String productionName) {
    productionTile(productionName).viewProduction();
    element(productionTitle).waitUntilDisplayed();
    return createPageInstance(ProductionsPage.class);
  }

  private ProductionTile productionTile(String productionName) {
    return new ProductionTile(driver, productionName);
  }
}

import com.google.inject.Inject;
import dataClient.CredentialsDataClient;
import models.Credentials;
import models.production.BasicInfo;
import models.production.ProductionSummary;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.productions.steps.DocumentSelectionSection;
import pages.productions.steps.ProductionsPage;
import pages.productions.steps.SummaryAndPreviewSection;

import java.util.UUID;

import static constants.production.ProductionStep.DOCUMENT_SELECTION;
import static constants.production.ProductionStep.SUMMARY_AND_PREVIEW;

public class ProductionsTest extends BaseTest {

  @Inject private CredentialsDataClient credentialsDataClient;

  @Test(description = "RPMXCON-47718", groups = "Productions")
  public void validateDeletingProduction() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();

    BasicInfo basicInfo =
        BasicInfo.builder()
            //
            .name("RPMXCON-47718" + UUID.randomUUID())
            .build();

    boolean productionPresentAfterDelete =
        Page(LoginPage.class)
            //
            .login(credentials)
            .navigateToProductions()
            .navigateToAddProductionPage()
            .basicInfo()
            .addBasicInfo(basicInfo)
            .save(HomePage.class)
            .navigateToProductions()
            .deleteProduction(basicInfo.getName())
            .isProductionAvailable(basicInfo.getName());

    Assert.assertFalse(productionPresentAfterDelete);
  }

  @Test(description = "RPMXCON-47716", groups = "Productions")
  public void validateProductionSummary() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();

    String production = "TD_DemoProduction";

    int documentsCount =
        Page(LoginPage.class)
            //
            .login(credentials)
            .navigateToProductions()
            .viewProduction(production)
            .navigateBackToStep(DOCUMENT_SELECTION, DocumentSelectionSection.class)
            .getTotalDocumentsCount();

    ProductionSummary summary =
        Page(ProductionsPage.class)
            .navigateForwardToStep(SUMMARY_AND_PREVIEW, SummaryAndPreviewSection.class)
            .getSummary();

    Assert.assertEquals(summary.getTotalDocuments(), documentsCount);
  }
}

package pages.reports;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

public class ReportsPage extends BasePage {

    @Inject
    public ReportsPage(WebDriver driver) {
        super(driver);
    }

    private final By tallyReportLink = queryByText("Tally Report");

    public TallyReportPage navigateToTallyReports() {
        element(tallyReportLink).click();
        element(getTitleLocator("Tally")).waitUntilDisplayed();
        return createPageInstance(TallyReportPage.class);
    }
}

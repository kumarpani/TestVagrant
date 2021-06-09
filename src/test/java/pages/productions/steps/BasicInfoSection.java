package pages.productions.steps;

import com.google.inject.Inject;
import models.production.BasicInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class BasicInfoSection extends ProductionsPage {

  @Inject
  public BasicInfoSection(WebDriver driver) {
    super(driver);
  }

  private final By productionNameTextBox = queryById("ProductionName");
  private final By productionDescriptionTextBox = queryById("ProductionDescription");
  private final By templateSelect = queryById("ddlTemplate");
  private final By title = queryByText("Basic Info & Select Template");

  public BasicInfoSection addBasicInfo(BasicInfo basicInfo) {
    element(title).waitUntilDisplayed();
    textbox(productionNameTextBox).setText(basicInfo.getName());

    if (Objects.nonNull(basicInfo.getDescription())) {
      textbox(productionDescriptionTextBox).setText(basicInfo.getDescription());
    }

    if (Objects.nonNull(basicInfo.getTemplate()) && Objects.nonNull(basicInfo.getTemplateGroup())) {
      selectWithGroups(templateSelect)
          .selectOption(basicInfo.getTemplateGroup(), basicInfo.getTemplate());
    }

    return this;
  }
}

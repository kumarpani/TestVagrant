package pages.productions.steps;

import com.google.inject.Inject;
import constants.production.ProductionStep;
import core.BasePage;
import core.controls.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductionsPage extends BasePage {

  @Inject
  public ProductionsPage(WebDriver driver) {
    super(driver);
  }

  private final By nextButton = query("[id*='next' i]");
  private final By saveButton = query("[id*='save' i]");
  private final By markCompleteButton = query("[id*='markComplete' i]");
  private final By markInCompleteButton = query("[id*='markInComplete' i]");
  private final By stepsUnorderedList = queryById("menu");
  private final By currentStep = query(".current-crumb");
  private final By backButton = query("//a[contains(text(), 'Back')]");

  public <T> T next(Class<T> tClass) {
    next();
    return createPageInstance(tClass);
  }

  public <T> T back(Class<T> tClass) {
    back();
    return createPageInstance(tClass);
  }

  public <T> T save(Class<T> tClass) {
    element(saveButton).click();
    return createPageInstance(tClass);
  }

  public ProductionsPage markComplete() {
    element(markCompleteButton).click();
    return this;
  }

  public ProductionsPage markInComplete() {
    element(markInCompleteButton).click();
    return this;
  }

  public BasicInfoSection basicInfo() {
    return createPageInstance(BasicInfoSection.class);
  }

  public String currentStep() {
    return unOrderedList(stepsUnorderedList).getListItem(currentStep).getTextValue();
  }

  public <T> T navigateBackToStep(ProductionStep step, Class<T> tClass) {
    T nextPage = createPageInstance(tClass);

    String current = currentStep();
    int currentStepIndex = ProductionStep.getStep(current.toUpperCase()).getIndex();
    int expectedStepIndex = step.getIndex();

    if (current.equalsIgnoreCase(step.getName())) {
      return nextPage;
    }

    if (expectedStepIndex > currentStepIndex) {
      throw new RuntimeException(
          String.format(
              "'%s' is ahead of current step '%s' and cannot be navigated back",
              step.getName(), current));
    }

    int stepsToNavigateBack = currentStepIndex - expectedStepIndex;
    for (int counter = 0; counter < stepsToNavigateBack; counter++) {
      back();
    }

    return nextPage;
  }

  public <T> T navigateForwardToStep(ProductionStep step, Class<T> tClass) {
    T nextPage = createPageInstance(tClass);

    String current = currentStep();
    int currentStepIndex = ProductionStep.getStep(current.toUpperCase()).getIndex();
    int expectedStepIndex = step.getIndex();

    if (current.equalsIgnoreCase(step.getName())) {
      return nextPage;
    }

    if (expectedStepIndex < currentStepIndex) {
      throw new RuntimeException(
          String.format(
              "'%s' is behind of current step '%s' and cannot navigate forward",
              step.getName(), current));
    }

    int stepsToNavigate = expectedStepIndex - currentStepIndex;
    for (int counter = 0; counter < stepsToNavigate; counter++) {
      next();
    }

    return nextPage;
  }

  private void back() {
    element(backButton).click();
  }

  private void next() {
    Element next = element(nextButton);
    if (next.hasAttribute("disabled")) {
      throw new RuntimeException("Next is disabled");
    }
    next.click();
  }
}

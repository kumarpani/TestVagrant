package constants.production;

import java.util.Arrays;

public enum ProductionStep {
  BASIC_INFO("Basic Info", 0),
  PRODUCTION_COMPONENTS("Production Components", 1),
  NUMBERING_AND_SORTING("Numbering & Sorting ", 2),
  DOCUMENT_SELECTION("Document Selection", 3),
  PRIV_GUARD("Priv Guard", 4),
  PRODUCTION_LOCATION("Production Location", 5),
  SUMMARY_AND_PREVIEW("Summary & Preview", 6),
  GENERATE("Generate", 7),
  QUALITY_CONTROL_AND_CONFIRMATION("Quality Control & Confirmation", 8);

  private final String step;
  private final int index;

  ProductionStep(String step, int index) {
    this.step = step;
    this.index = index;
  }

  public String getName() {
    return step;
  }

  public int getIndex() {
    return index;
  }

  public static ProductionStep getStep(String name) {
    return Arrays.stream(ProductionStep.values())
        .filter(step -> step.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new RuntimeException(name + " not a valid step"));
  }
}

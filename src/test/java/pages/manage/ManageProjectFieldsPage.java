package pages.manage;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static constants.grid.ProjectFieldsGridColumns.FIELD_NAME;
import static constants.grid.ProjectFieldsGridColumns.IS_SEARCHABLE;

public class ManageProjectFieldsPage extends BasePage {

  @Inject
  public ManageProjectFieldsPage(WebDriver driver) {
    super(driver);
  }

  private final By projectFieldsGrid = queryById("ProjectFieldsDataTable");

  public Map<String, List<String>> getSearchableAndNonSearchableFields() {
    Map<String, List<String>> gridValues = grid(projectFieldsGrid).getAllColumnValuesOfAllPages();
    List<String> isSearchableValues = gridValues.get(IS_SEARCHABLE);
    List<String> fieldNames = gridValues.get(FIELD_NAME);

    List<String> searchableFields = new ArrayList<>();
    List<String> unsearchableFields = new ArrayList<>();

    for (int counter = 0; counter < isSearchableValues.size(); counter++) {
      String fieldName = fieldNames.get(counter);
      if (isSearchableValues.get(counter).equalsIgnoreCase("True")) {
        searchableFields.add(fieldName);
      } else {
        unsearchableFields.add(fieldName);
      }
    }

    return Map.of("SEARCHABLE", searchableFields, "UNSEARCHABLE", unsearchableFields);
  }
}

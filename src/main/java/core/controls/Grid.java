package core.controls;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid extends Element {

  private final String rowSelector = "tbody tr";
  private final String columnValueSelector =
      ".//td[COLUMN_INDEX][contains(text(), 'COLUMN_VALUE')]";

  public Grid(WebDriver driver, String idSelector) {
    super(driver, By.cssSelector(String.format("[id*='%s' i]", idSelector)));
  }

  public Grid(WebDriver driver, By locator) {
    super(driver, locator);
  }

  public List<String> getColumnNames() {
    String columnNameSelector = "thead th";
    By columnNames = new ByChained(locator, query(columnNameSelector));
    return new ElementCollection(driver, columnNames).getAttributeValues("innerText");
  }

  public int getDataRowsCount() {
    By rowsSelector = new ByChained(locator, query(rowSelector));
    return new ElementCollection(driver, rowsSelector).waitUntilPresent().count();
  }

  public List<String> getColumnValues(String columnName) {
    List<String> columnValues = new ArrayList<>();
    By rowsSelector = new ByChained(locator, query(rowSelector));
    List<WebElement> rows = new ElementCollection(driver, rowsSelector).get();
    int columnIndex = getColumnIndex(columnName);
    rows.forEach(
        row -> {
          WebElement cell = row.findElement(query(String.format(".//td[%s]", columnIndex)));
          columnValues.add(cell.getText());
        });

    return columnValues;
  }

  public String getColumnValue(String columnName, Map<String, String> searchCriteria) {
    By expectedRowSelector = query(buildRowSelector(searchCriteria));
    Element row = new Element(driver, expectedRowSelector);

    if (row.isPresent(Duration.ofSeconds(5))) {
      return row.find(query(String.format(".//td[%s]", getColumnIndex(columnName))))
          .getText()
          .trim();
    }

    throw new RuntimeException("No matching rows found for the search criteria");
  }

  public Map<String, List<String>> getAllColumnValuesOfTheCurrentPage() {
    Map<String, List<String>> columnValues = new HashMap<>();
    List<String> columnNames =
        getColumnNames().stream().filter(column -> !column.isEmpty()).collect(Collectors.toList());

    Map<String, Integer> columnNameAndIndexMap = new HashMap<>();

    columnNames.forEach(
        column -> {
          columnNameAndIndexMap.put(column, getIndex(columnNames, column) + 1);
          columnValues.put(column, new ArrayList<>());
        });

    By rowsSelector = new ByChained(locator, query(rowSelector));
    List<WebElement> rows = new ElementCollection(driver, rowsSelector).get();
    rows.forEach(
        row -> {
          columnNameAndIndexMap.forEach(
              (column, index) -> {
                String cellValue =
                    row.findElement(query(String.format(".//td[%s]", index))).getText().trim();
                columnValues.get(column).add(cellValue);
              });
        });

    return columnValues;
  }

  public Map<String, List<String>> getAllColumnValuesOfAllPages() {
    Map<String, List<String>> columnValues = new HashMap<>();
    Map<String, Integer> columnNameAndIndexMap = new HashMap<>();

    List<String> columnNames =
        getColumnNames().stream().filter(column -> !column.isEmpty()).collect(Collectors.toList());

    columnNames.forEach(
        column -> {
          columnNameAndIndexMap.put(column, getIndex(columnNames, column) + 1);
          columnValues.put(column, new ArrayList<>());
        });

    String lastPageSelector = "//li[.//*[text() = 'Next']]//preceding-sibling::*[position()= 1]//a";
    Element lastPage = new Element(driver, new ByChained(locator, query(lastPageSelector)));
    int totalPages =
        lastPage.isPresent(Duration.ofSeconds(5)) ? Integer.parseInt(lastPage.getTextValue()) : 2;

    for (int pageCounter = 1; pageCounter <= totalPages; pageCounter++) {
      By rowsSelector = new ByChained(locator, query(rowSelector));
      List<WebElement> rows = new ElementCollection(driver, rowsSelector).get();
      rows.forEach(
          row -> {
            columnNameAndIndexMap.forEach(
                (column, index) -> {
                  String cellValue =
                      row.findElement(query(String.format(".//td[%s]", index))).getText().trim();
                  columnValues.get(column).add(cellValue);
                });
          });

      clickPage(pageCounter + 1);
    }

    return columnValues;
  }

  public void selectRow(Map<String, String> searchCriteria) {
    waitForRowsLoaded();
    String selector = buildRowSelector(searchCriteria);
    Element expectedRow = new Element(driver, new ByChained(locator, query(selector)));

    if (expectedRow.isPresent(Duration.ofSeconds(10))) {
      By radioButtonSelector =
          new ByChained(locator, query(String.format("%s//td[1]//i", selector)));
      Element radioButton = new Element(driver, radioButtonSelector);
      radioButton.click();
      return;
    }

    throw new RuntimeException("No rows matching search criteria");
  }

  public boolean rowPresent(Map<String, String> searchCriteria) {
    waitForRowsLoaded();
    String selector = buildRowSelector(searchCriteria);
    Element expectedRow = new Element(driver, new ByChained(locator, query(selector)));
    return expectedRow.isPresent(Duration.ofSeconds(10));
  }

  private void clickPage(int page) {
    By selector =
        query(String.format("//li[contains(@class , 'paginate_button')]//a[text() = '%s']", page));
    Element nextPage = new Element(driver, new ByChained(locator, selector));
    if (nextPage.isPresent()) {
      nextPage.click();
      waitForRowsLoaded();
    }
  }

  private String buildRowSelector(Map<String, String> searchCriteria) {
    List<String> searchCriteriaSelector = new ArrayList<>();

    searchCriteria.forEach(
        (column, value) -> {
          String columnIndex = String.valueOf(getColumnIndex(column));
          String filterSelector =
              columnValueSelector
                  .replaceAll("COLUMN_INDEX", columnIndex)
                  .replaceAll("COLUMN_VALUE", value);
          searchCriteriaSelector.add(filterSelector);
        });

    StringBuilder selectorBuilder = new StringBuilder();
    selectorBuilder.append("//tbody//tr");
    searchCriteriaSelector.forEach(
        filter -> {
          selectorBuilder.append(String.format("[%s]", filter));
        });

    return selectorBuilder.toString();
  }

  private void waitForRowsLoaded() {
    try {
      By rowsSelector = new ByChained(locator, query(rowSelector));
      Awaitility.waitAtMost(Duration.ofSeconds(5))
          .until(() -> new ElementCollection(driver, rowsSelector).count() > 0);
    } catch (Exception ex) {
      throw new RuntimeException("No rows found in the grid");
    }
  }

  private int getColumnIndex(String columnName) {
    List<String> columns = getColumnNames();
    return getIndex(columns, columnName) + 1;
  }

  public int getIndex(List<String> items, String itemText) {
    return IntStream.range(0, items.size())
        .filter(index -> items.get(index).equalsIgnoreCase(itemText))
        .findFirst()
        .orElse(-10);
  }
}

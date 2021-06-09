package pages.search;

import com.google.inject.Inject;
import core.BasePage;
import core.controls.SearchResultsTile;
import core.controls.TextBox;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static constants.search.SearchResultsTileTitle.CONCEPTUALLY_SIMILAR;
import static constants.search.SearchResultsTileTitle.DOCS;

public class SearchPage extends BasePage {

  private final By metaDataButton = queryById("metadataHelper");
  private final By searchTextBox =
      query(".textboxlist:not([style='display: none;']) .textboxlist-bits input");
  private final By searchButton = queryById("btnBasicSearch");
  private final By saveButton = queryById("btnSaveSearch");
  private final By resultsCart = queryById("resultsCart");
  private final By actionsButton = query("#ddlbulkactions button");
  private final By actionsItemsUnOrderList = query("#ddlbulkactions ul");
  private final By sessionSearchUnorderedList = query("#sessionSearchList");
  private final By sessionSearchListFullName = query(".SrchTextfulltext");

  @Inject
  public SearchPage(WebDriver driver) {
    super(driver);
  }

  public SearchPage search(String searchText) {
    TextBox textbox = textbox(searchTextBox);
    waitForLoading();
    textbox.click();
    textbox.setText(searchText);
    element(searchButton).click();
    waitForLoading();
    return this;
  }

  public SearchPage search() {
    element(searchButton).click();
    waitForLoading();
    return this;
  }

  public MetaDataDialog viewMetaData() {
    element(metaDataButton).click();
    return createPageInstance(MetaDataDialog.class);
  }

  public int getDocsSearchResultCount() {
    String count = searchResultsTile(DOCS).getCount();
    if (!count.isEmpty()) {
      return Integer.parseInt(count);
    }

    return 0;
  }

  public List<String> getSearchResultsColumnValues(String columnName) {
    String searchResultsGrid = "taskbasicPureHits";
    return grid(searchResultsGrid).getColumnValues(columnName);
  }

  public SaveSearchDialog save() {
    element(saveButton).click();
    return createPageInstance(SaveSearchDialog.class);
  }

  public SearchPage dragDropTilesToSelectedResults(String tile) {
    searchResultsTile(tile).dragDrop(element(resultsCart));
    waitForLoading();
    return this;
  }

  public SearchPage dragDropTilesToSelectedResults(List<String> tiles) {
    tiles.forEach(tile -> searchResultsTile(tile).addTileToResults());
    return this;
  }

  public SearchPage performConceptuallySimilarSearch() {
    searchResultsTile(CONCEPTUALLY_SIMILAR).click();
    element(queryByText("Conceptual Similar search started successfully.")).waitUntilDisplayed();
    return this;
  }

  public List<String> getTilesAddedInResults() {
    return unOrderedList(resultsCart).getAttributeOfListItemDescendants(query("a[data-original-title]"), "data-original-title");
  }

  public <T> T performAction(String action, Class<T> tClass) {
    element(actionsButton).click();
    unOrderedList(actionsItemsUnOrderList).clickListItemMatchingText(action);
    waitForLoading();
    return createPageInstance(tClass);
  }

  public List<String> getSessionSearchList() {
    return unOrderedList(sessionSearchUnorderedList)
        .getTextOfListItemDescendants(sessionSearchListFullName);
  }

  private SearchResultsTile searchResultsTile(String title) {
    return new SearchResultsTile(driver, title);
  }
}

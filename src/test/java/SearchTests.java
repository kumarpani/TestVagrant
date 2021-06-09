import com.google.inject.Inject;
import dataClient.CredentialsDataClient;
import models.Credentials;
import models.search.MetaData;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.util.List;
import java.util.Map;

import static constants.search.SearchMetaData.CUSTODIAN_NAME;
import static constants.search.SearchResultsTileTitle.*;

public class SearchTests extends BaseTest {

  @Inject private CredentialsDataClient credentialsDataClient;

  @Test(
      description = "RPMXCON-48868, RPMXCON-48863",
      groups = {"search", "BasicSearch"})
  public void validateSearchableAndUnsearchableFields() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();

    Map<String, List<String>> fields =
        Page(LoginPage.class)
            //
            .login(credentials)
            .navigateToProjectFields()
            .getSearchableAndNonSearchableFields();

    List<String> searchableFields =
        Page(HomePage.class)
            //
            .navigateToSessionSearch()
            .viewMetaData()
            .getMetaDataFields();

    List<String> searchableFieldsMismatch =
        getDifferences(fields.get("SEARCHABLE"), searchableFields);
    List<String> unSearchableFieldsMismatch =
        getDifferences(fields.get("UNSEARCHABLE"), searchableFields);

    Assert.assertEquals(
        searchableFieldsMismatch.size(),
        0,
        "Searchable fields Mismatch:\n" + StringUtils.join(searchableFieldsMismatch, ","));

    Assert.assertEquals(
        fields.get("UNSEARCHABLE").size(),
        unSearchableFieldsMismatch.size(),
        "UnSearchable fields Mismatch:\n" + StringUtils.join(unSearchableFieldsMismatch, ","));
  }

  @Test(
      description = "RPMXCON-48465-Advanced Search",
      groups = {"search", "AdvancedSearch"})
  public void validateTilesAddedToResultsAreRetainedOnConceptualSearch() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();

    MetaData metaData = MetaData.builder().field(CUSTODIAN_NAME).value("Andrew").build();
    List<String> tilesToBeAddedToResults =
        List.of(DOCS, THREADED_DOCUMENTS, NEAR_DUPLICATES, FAMILY_MEMBERS);

    List<String> tilesInResults =
        Page(LoginPage.class)
            //
            .login(credentials)
            .navigateToSessionSearch()
            .viewMetaData()
            .insertMetaData(metaData)
            .search()
            .dragDropTilesToSelectedResults(tilesToBeAddedToResults)
            .performConceptuallySimilarSearch()
            .getTilesAddedInResults();

    List<String> differences = getDifferences(tilesInResults, tilesToBeAddedToResults);

    Assert.assertEquals(
        differences.size(),
        0,
        StringUtils.join(tilesToBeAddedToResults)
            + " not retained after conceptually similar search");
  }
}

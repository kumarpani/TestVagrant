import com.google.inject.Inject;
import constants.SystemProperties;
import dataClient.CredentialsDataClient;
import models.Credentials;
import models.ImpersonationDetails;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DocViewPage;
import pages.HomePage;
import pages.LoginPage;
import pages.search.SavedSearchPage;
import pages.search.SearchPage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static constants.ImpersonateTo.REVIEW_MANAGER;
import static constants.NotificationsText.EXPORT_READY_FOR_DOWNLOAD;
import static constants.grid.SavedSearchGridColumns.COUNT_OF_RESULTS;
import static constants.grid.SavedSearchGridColumns.SEARCH_NAME;
import static constants.search.SearchResultsAction.VIEW_IN_DOC_VIEW;
import static constants.search.SearchResultsTileTitle.DOCS;

public class SavedSearchTests extends BaseTest {

  @Inject private CredentialsDataClient credentialsDataClient;

  @Test(
      description = "RPMXCON-47948, RPMXCON-47947, RPMXCON-47945",
      groups = {"search", "SavedSearch"})
  public void validateCorrectNumberOfDocumentsAppearsInSavedSearch() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();
    String searchText = "proximity";
    String searchName = "RPMXCON-47948-bc257037-2352-43a3-a8f2-3d5f42aca289";

    // Commented out to avoid creation of saved searches on every execution
    // String searchName = String.format("RPMXCON-47948-%s", UUID.randomUUID());

    Map<String, String> searchCriteria = new HashMap<>(Map.of(SEARCH_NAME, searchName));

    Page(LoginPage.class)
        //
        .login(credentials)
        .navigateToSavedSearch();

    Map<String, String> savedSearchCriteria = createSavedSearch(searchCriteria, searchText);
    Page(SavedSearchPage.class).editSavedSearch(searchCriteria);

    SearchPage searchPage = Page(SearchPage.class);

    List<String> sessionSearchList = searchPage.getSessionSearchList();
    Assert.assertTrue(sessionSearchList.contains(String.format("SS: %s", searchName)));

    int documentsCount =
        searchPage
            //
            .dragDropTilesToSelectedResults(DOCS)
            .performAction(VIEW_IN_DOC_VIEW, DocViewPage.class)
            .getDocsCount();

    Assert.assertEquals(
        documentsCount, Integer.parseInt(savedSearchCriteria.get(COUNT_OF_RESULTS)));
  }

  @Test(
      description = "RPMXCON-47732",
      groups = {"search", "SavedSearch"})
  public void validateExportingSavedSearch() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();
    String searchText = "proximity";
    String searchName = "RPMXCON-47948-bc257037-2352-43a3-a8f2-3d5f42aca289";

    // Commented out to avoid creation of saved searches on every execution
    // String searchName = String.format("RPMXCON-47948-%s", UUID.randomUUID());

    Map<String, String> searchCriteria = new HashMap<>(Map.of(SEARCH_NAME, searchName));
    List<String> metaData = List.of("CustodianName", "DocFileExtension");

    File downloadDirectory = new File(SystemProperties.USER_DIR, "build/downloads");
    int fileCount = Objects.requireNonNull(downloadDirectory.list()).length;

    Page(LoginPage.class)
        //
        .login(credentials)
        .navigateToSavedSearch();

    Map<String, String> savedSearchCriteria = createSavedSearch(searchCriteria, searchText);

    int initialNotificationsCount =
        Page(HomePage.class)
            .expandNotifications()
            .getNotificationsCountFor(EXPORT_READY_FOR_DOWNLOAD);

    Page(HomePage.class).collapseNotifications();

    Page(SavedSearchPage.class)
        //
        .export(savedSearchCriteria)
        .addMetaDataFieldsAndRunReport(metaData, HomePage.class)
        .expandNotifications()
        .waitForNotificationCountToBeMoreThan(EXPORT_READY_FOR_DOWNLOAD, initialNotificationsCount)
        .clickNotification(EXPORT_READY_FOR_DOWNLOAD, HomePage.class)
        .collapseNotifications();

    int fileCountAfterDownload = Objects.requireNonNull(downloadDirectory.list()).length;

    Assert.assertNotEquals(fileCountAfterDownload, fileCount);
  }

  @Test(
      description = "RPMXCON-48934",
      groups = {"search"})
  public void validateReleasingDocumentsFromTallyAndViewingDocumentsInSearch() {
    Credentials credentials = credentialsDataClient.getProjectAdministrator();
    String securityGroup = "RPMXCON-48934";
    String defaultSecurityGroup = "Default Security Group";
    String tallyMetadataField = "CustodianName";

    Map<String, String> searchCriteria = new HashMap<>(Map.of(SEARCH_NAME, "RPMXCON-48934"));

    ImpersonationDetails reviewManager =
        ImpersonationDetails.builder()
            .impersonateTo(REVIEW_MANAGER)
            .domain("TestVagrant")
            .project("TestVagrant_Smoke")
            .securityGroup(securityGroup)
            .build();

    Page(LoginPage.class)
        //
        .login(credentials)
        .navigateToSecurityGroup()
        .createSecurityGroup(securityGroup, true, HomePage.class)
        .navigateToSavedSearch();

    Map<String, String> savedSearchCriteria = createSavedSearch(searchCriteria, "*", true);

    Page(HomePage.class)
        .navigateToReports()
        .navigateToTallyReports()
        .selectSecurityGroupSource(defaultSecurityGroup)
        .selectMetaData(tallyMetadataField)
        .applyTally()
        .bulkReleaseAllRecords(securityGroup);

    int docsResultCount =
        Page(HomePage.class)
            .changeRole()
            .impersonateAs(reviewManager)
            .navigateToSessionSearch()
            .search("*")
            .getDocsSearchResultCount();

    Assert.assertEquals(
        docsResultCount, Integer.parseInt(savedSearchCriteria.get(COUNT_OF_RESULTS)));
  }

  private Map<String, String> createSavedSearch(
      Map<String, String> searchCriteria, String saveSearchQuery, boolean deleteIfExists) {
    boolean savedSearchPresent = Page(SavedSearchPage.class).isSavedSearchPresent(searchCriteria);

    if (savedSearchPresent && !deleteIfExists) {
      String documentsCount =
          Page(SavedSearchPage.class).getColumnValue(COUNT_OF_RESULTS, searchCriteria);
      searchCriteria.put(COUNT_OF_RESULTS, documentsCount);
      return searchCriteria;
    }

    if (savedSearchPresent) {
      Page(SavedSearchPage.class).deleteSavedSearch(searchCriteria);
    }

    int documentsCount =
        Page(HomePage.class)
            //
            .navigateToSessionSearch()
            .search(saveSearchQuery)
            .getDocsSearchResultCount();

    Assert.assertTrue(documentsCount >= 1);

    Page(SearchPage.class)
        //
        .save()
        .saveAsNewMySavedSearch(searchCriteria.get(SEARCH_NAME), HomePage.class)
        .navigateToSavedSearch();

    searchCriteria.put(COUNT_OF_RESULTS, String.valueOf(documentsCount));
    return searchCriteria;
  }

  private Map<String, String> createSavedSearch(
      Map<String, String> searchCriteria, String saveSearchQuery) {
    return createSavedSearch(searchCriteria, saveSearchQuery, false);
  }
}

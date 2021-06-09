import com.google.inject.Guice;
import com.google.inject.Injector;
import core.BrowserDriver;
import driverManager.injection.WebModule;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.stream.Collectors;

@org.testng.annotations.Guice()
public class BaseTest {

  private Injector webDriverInjector;

  @BeforeMethod(alwaysRun = true)
  public void launchApplication() {
    webDriverInjector = Guice.createInjector(new WebModule());
  }

  public <T> T Page(Class<T> tClass) {
    return webDriverInjector.getInstance(tClass);
  }

  @AfterMethod(alwaysRun = true)
  public void logOut() {
    BrowserDriver browserDriver = webDriverInjector.getInstance(BrowserDriver.class);
    browserDriver.navigateToUrl("https://sightlinept.consilio.com/Login/Logout");
    browserDriver.waitForPageToBeReady();
    webDriverInjector.getInstance(WebDriver.class).quit();
  }

  protected List<String> getDifferences(List<String> actual, List<String> expected) {
    return actual.parallelStream()
        .filter(item -> !expected.contains(item))
        .collect(Collectors.toList());
  }
}

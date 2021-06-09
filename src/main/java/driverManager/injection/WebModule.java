package driverManager.injection;

import com.google.inject.AbstractModule;
import org.openqa.selenium.WebDriver;

public class WebModule extends AbstractModule {

  @Override
  public void configure() {
    // bind driver
    bind(WebDriver.class).toProvider(DriverProvider.class).asEagerSingleton();
  }
}

package driverManager.injection;

import com.google.inject.Provider;
import driverManager.DriverManager;
import org.openqa.selenium.WebDriver;

public class DriverProvider implements Provider<WebDriver> {

    @Override
    public WebDriver get() {
        return new DriverManager().createDriver();
    }
}


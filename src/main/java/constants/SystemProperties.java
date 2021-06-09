package constants;

public class SystemProperties {
  public static final String BROWSER = System.getProperty("browser", "chrome");
  public static final boolean HEADLESS =
      Boolean.parseBoolean(System.getProperty("headless", "false"));
  public static final String RUN_MODE = System.getProperty("runMode", "local");
  public static final String ENV = System.getProperty("env", "qa");
  public static final String USER_DIR = System.getProperty("user.dir");
}

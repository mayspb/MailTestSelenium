package mail.fw;

import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  public WebDriver driver;
  public WebDriverWait wait;

  public String mailPageURL;
  public String login;
  public String password;
  public String messageID;
  public String subject;
  public String sender;
  public String filesDir;
  public String outputDir;

  private NavigationHelper navigationHelper;
  private LoginPageHelper loginPageHelper;
  private MailPageHelper mailPageHelper;
  private HomePageHelper homePageHelper;

  private Properties properties;

  public ApplicationManager(Properties properties) {
    this.properties = properties;
    String browser = properties.getProperty("browser");
    if ("firefox".equals(browser)) {
      FirefoxDriverManager.getInstance().setup();
      driver = new FirefoxDriver();
    } else if ("ie".equals(browser)) {
      InternetExplorerDriverManager.getInstance().setup();
      driver = new InternetExplorerDriver();
    } else if ("chrome".equals(browser)) {
      ChromeDriverManager.getInstance().setup();
      driver = new ChromeDriver();
    } else if ("opera".equals(browser)) {
      OperaDriverManager.getInstance().setup();
      driver = new OperaDriver();
    } else if ("edge".equals(browser)) {
      EdgeDriverManager.getInstance().setup();
      driver = new EdgeDriver();
    } else {
      throw new Error("Unsupported browser: " + browser);
    }
    driver.manage().window().maximize();
    driver.manage().deleteAllCookies();

    mailPageURL = properties.getProperty("mailPageURL");
    login = properties.getProperty("login");
    password = properties.getProperty("password");
    messageID = properties.getProperty("messageID");
    subject = properties.getProperty("subject");
    sender = properties.getProperty("sender");
    filesDir = System.getProperty("filesDir", "files");
    outputDir = System.getProperty("outputDir", "test-output");

    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, 10);
  }

  public NavigationHelper getNavigationHelper() {
    if (navigationHelper == null) {
      navigationHelper = new NavigationHelper(this);
    }
    return navigationHelper;
  }

  public LoginPageHelper getLoginPageHelper() {
    if (loginPageHelper == null) {
      loginPageHelper = new LoginPageHelper(this);
    }
    return loginPageHelper;
  }

  public MailPageHelper getMailPageHelper() {
    if (mailPageHelper == null) {
      mailPageHelper = new MailPageHelper(this);
    }
    return mailPageHelper;
  }

  public HomePageHelper getHomePageHelper() {
    if (homePageHelper == null) {
      homePageHelper = new HomePageHelper(this);
    }
    return homePageHelper;
  }

  public void stop() {
    driver.quit();
  }
}
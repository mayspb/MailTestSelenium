package mail.fw;

import org.openqa.selenium.By;
import org.testng.Assert;

public class HomePageHelper extends HelperBase {
  By authButton = By.xpath("//button[contains(@class,'auth__button')]");

  public HomePageHelper(ApplicationManager manager) {
    super(manager);
  }

  public void checkAuthIsOff() {
    Assert.assertTrue( driver.findElements(authButton).size() != 0, "Auth session is still alive!");
  }

}

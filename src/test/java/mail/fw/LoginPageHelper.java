package mail.fw;

import org.openqa.selenium.By;

public class LoginPageHelper extends HelperBase {
  By usernameLocator = By.name("login");
  By passwordLocator = By.name("passwd");
  By loginButtonLocator = By.xpath("//button[@type='submit']");
  By enterButton = By.xpath("//div[@class='new-hr-auth-Form_Line']//a[2]");

  public LoginPageHelper(ApplicationManager manager) {
    super(manager);
  }

  public void enterButton() {
    click(enterButton);
  }

  public void loginUser() {
    type(usernameLocator, manager.login);
    type(passwordLocator, manager.password);
    submit(loginButtonLocator);
  }

}

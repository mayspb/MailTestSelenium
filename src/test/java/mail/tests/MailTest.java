package mail.tests;

import org.testng.annotations.Test;

public class MailTest extends TestBase {

  @Test
  public void testLoginAndMessage() {
    app.getNavigationHelper().openMailPage();
    app.getLoginPageHelper().enterButton();
    app.getLoginPageHelper().loginUser();
    app.getMailPageHelper().checkAuth();
    app.getMailPageHelper().openMessage();
    app.getMailPageHelper().checkSubject();
    app.getMailPageHelper().checkSender();
    app.getMailPageHelper().checkMessageBody();
    app.getMailPageHelper().logOut();
    app.getHomePageHelper().checkAuthIsOff();
  }

}

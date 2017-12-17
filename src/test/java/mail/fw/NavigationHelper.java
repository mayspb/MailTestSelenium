package mail.fw;

public class NavigationHelper extends HelperBase {
  public NavigationHelper(ApplicationManager manager) {
    super(manager);
  }

  public void openMailPage() {
    logger.debug("Loading " + manager.mailPageURL);
    driver.get(manager.mailPageURL);
  }
}

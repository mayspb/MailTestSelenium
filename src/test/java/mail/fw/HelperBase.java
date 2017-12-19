package mail.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HelperBase {
  protected ApplicationManager manager;
  protected WebDriver driver;
  protected WebDriverWait wait;

  protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  public HelperBase(ApplicationManager manager) {
    this.manager = manager;
    this.driver = manager.driver;
    this.wait = manager.wait;
  }

  protected void type(By locator, String text) {
    clear(locator);
    logText(locator, text);
    driver.findElement(locator).sendKeys(text);
  }

  protected void click(By locator) {
    waitClickable(locator);
    logger.debug("Click on element '{}'", locator);
    try {
      driver.findElement(locator).click();
    } catch (StaleElementReferenceException e) {
      logger.debug("Catch " + e.getMessage() + "\nWill try second attempt.");
      logger.debug("Click on element '{}'", locator);
      driver.findElement(locator).click();
    }
  }

  protected void submit(By locator) {
    logger.debug("Submit on element '{}'", locator);
    driver.findElement(locator).submit();
  }

  protected void clear(By locator) {
    logger.debug("Clear text in element {}", locator);
    driver.findElement(locator).clear();
  }

  private void logText(By locator, String text) {
    if (locator.toString().toLowerCase().contains("pass")) {
      text = text.replaceAll(".", "*");
    }
    logger.debug("Type '{}' in: {}", text, locator);
  }

  protected String getText(By locator) {
    logger.debug("Get text from element '{}'", locator);
    return driver.findElement(locator).getText();
  }

  protected void waitPresence(By locator) {
    logger.debug("Wait presence of element located by '{}'", locator);
    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
  }

  protected void waitClickable(By locator) {
    logger.debug("Wait element to be clickable located by '{}'", locator);
    wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected String getInnerHTML(By locator) {
    logger.debug("Get attribute innerHTML from element {}", locator);
    return driver.findElement(locator).getAttribute("innerHTML");
  }

}

package mail.fw;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.fail;

public class MailPageHelper extends HelperBase {
  String fNameControl = manager.filesDir + "/messageBodyControl.xml";
  String fNameTest = manager.outputDir + "/messageBodyTest.xml";
  By subjLocator = By.xpath("//div[contains(@class, 'mail-Message-Toolbar-Subject')]");
  By senderLocator = By.xpath("//span[contains(@class,'mail-Message-Sender-Email')]");
  By userNameLocator = By.xpath("//div[@class='mail-User-Name']");
  By searchContainerLocator = By.xpath("//div[@class='mail-SearchContainer']//input");
  By searchButtonLocator = By.xpath("//div[@class='mail-SearchContainer']//button");
  By messagesSearchInfoLocator = By.xpath("//div[contains(@class,'mail-MessagesSearchInfo')]");
  By logOutButton = By.xpath("//a[contains(@href, 'action=logout')]");
  By messageLocator = By.xpath("//div[contains(@class, 'mail-MessagesList')]//span[contains(@title, '" + manager.subject + "')]/ancestor::a[1]");
  By advSearchLocator = By.xpath("//span[contains(@class, 'mail-SearchInput_IconContainer')][2]");
  String chooseFoldersLocator = "//div[@class='mail-AdvancedParams-Row'][2]";
  String advSearchButtonsLocator = "//span[@class='button2__text']";
  String containsTextInboxLocator = "[contains(text(), 'Входящие') or contains(text(), 'Inbox')]";
  By advSearchInboxLocator = By.xpath("//span[@class='menu__text']" + containsTextInboxLocator);


  public MailPageHelper(ApplicationManager manager) {
    super(manager);
  }

  public void openUserMenu() {
    click(userNameLocator);
  }

  public void checkAuth() {
    Assert.assertEquals(manager.login, getText(userNameLocator));
  }

  public void logOut() {
    openUserMenu();
    click(logOutButton);
  }

  public void searchMessage(String searchString) {
    click(searchContainerLocator);
    waitPresence(advSearchLocator);
    openAdvSearch();
    chooseInbox();
    type(searchContainerLocator, searchString);
    click(searchButtonLocator);
    waitPresence(messagesSearchInfoLocator);
  }

  public void openMessage() {
    searchMessage(manager.subject);
    click(messageLocator);
    try {
      waitPresence(subjLocator);
    } catch (TimeoutException e) {
      logger.debug("Catch " + e.getMessage() + "\nWill try second attempt.");
      click(messageLocator);
      waitPresence(subjLocator);
    }
  }

  public void checkSubject() {
    Assert.assertEquals(getText(subjLocator), manager.subject);
  }

  public void checkSender() {
    Assert.assertEquals(getText(senderLocator), manager.sender);
  }

  public String getMessageBody() {
    return driver.findElement(By.xpath("//div[@class='mail-Message-Body-Content']")).getAttribute("innerHTML");
  }

  public void writeMessageBody() throws IOException {
    FileUtils.writeStringToFile(new File(fNameTest), parserXHtml(getMessageBody()), "UTF-8");
  }

  public String parserXHtml(String html) {
    Document document = Jsoup.parseBodyFragment(Parser.unescapeEntities(html, false));
    document.outputSettings().charset("UTF-8");
    document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
    document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    return document.toString();
  }

  public boolean compareMessageBody() throws Exception {
    writeMessageBody();
    Diff myDiffSimilar = DiffBuilder.compare(Input.fromFile(fNameControl))
        .withTest(Input.fromFile(fNameTest))
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
        .build();
    logger.debug("XML similar " + myDiffSimilar.toString());
    return myDiffSimilar.hasDifferences();
  }

  public void checkMessageBody() {
    try {
      Assert.assertFalse(compareMessageBody(), "Message body is different!");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Something goes wrong: " + e.getMessage());
    }
  }

  public void openAdvSearch() {
    click(advSearchLocator);
    waitPresence(By.xpath(chooseFoldersLocator));
  }

  public void chooseInbox() {
    String text = getText(By.xpath(chooseFoldersLocator + advSearchButtonsLocator));
    if (!(text.contains("Входящие") || text.contains("Inbox"))) {
      click(By.xpath(chooseFoldersLocator));
      waitPresence(advSearchInboxLocator);
      click(advSearchInboxLocator);
      waitPresence(By.xpath(chooseFoldersLocator + advSearchButtonsLocator + containsTextInboxLocator));
    }
  }

}

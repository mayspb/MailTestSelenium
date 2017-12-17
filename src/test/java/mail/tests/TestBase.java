package mail.tests;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import mail.fw.ApplicationManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class TestBase {
  Logger logger = LoggerFactory.getLogger(this.getClass().getName());
  String logback = System.getProperty("logback.configurationFile", "logback.xml");
  protected ApplicationManager app;

  @BeforeTest
  public void setUp() throws Exception {
    String configFile = System.getProperty("configFile", "mail.properties");
    Properties properties = new Properties();
    properties.load(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
    app = new ApplicationManager(properties);
    initializeLogback();
  }

  public void initializeLogback() {
    File logbackFile = new File(logback);
    System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
    StaticLoggerBinder loggerBinder = StaticLoggerBinder.getSingleton();
    LoggerContext loggerContext = (LoggerContext) loggerBinder.getLoggerFactory();

    loggerContext.reset();
    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(loggerContext);
    try {
      configurator.doConfigure(logbackFile);
    } catch( JoranException e ) {
      logger.error(e.getMessage(), e);
    }
  }

  @AfterMethod
  public void tearDown(ITestResult result) {
    if (ITestResult.FAILURE == result.getStatus()) {
      try {
        TakesScreenshot ts = (TakesScreenshot) app.driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        String fileName = result.getName() + "_" + dateFormat.format(new Date()) + ".png";
        FileUtils.copyFile(source, new File(app.outputDir + "/Screenshots/" + fileName));
        logger.error("Test failed, Screenshot taken: " + fileName);
      } catch (Exception e) {
        logger.error("Exception while taking screenshot " + e.getMessage(), e);
      }
    }
    logger.info("Test was executed for " + (result.getEndMillis() - result.getStartMillis()) / 1000 + " seconds.");
  }

  @AfterTest
  public void tearDown() throws Exception {
    app.stop();
  }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.submarine;

import static org.junit.Assert.fail;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.Dimension;


public class WebDriverManager {

  public final static Logger LOG = LoggerFactory.getLogger(WebDriverManager.class);

  private static String downloadPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "downloads").toString();

  private static boolean webDriverIsDownloaded = false;

  private static String webDriverPath = "";

  public static WebDriver getWebDriver() {
    WebDriver driver = null;

    if (driver == null) {
      try {
        WebDriverProvider provide = new ChromeWebDriverProvider();
        driver = generateWebDriver(provide);
      } catch (Exception e) {
        LOG.error("Exception in WebDriverManager while ChromeDriver ", e);
      }
    }

    if (driver == null) {
      try {
        WebDriverProvider provide = new FirefoxWebDriverProvider();
        driver = generateWebDriver(provide);
      } catch (Exception e) {
        LOG.error("Exception in WebDriverManager while FireFox Driver ", e);
      }
    }


    String url;
    if (System.getenv("url") != null) {
      url = System.getenv("url");
    } else if ((System.getProperty("SUBMARINE_WORKBENCH_URL") != null) || (System.getProperty("SUBMARINE_WORKBENCH_PORT") == null)) {
      if (System.getProperty("SUBMARINE_WORKBENCH_URL") == null) {
        url = "http://127.0.0.1";
      } else {
        url = System.getProperty("SUBMARINE_WORKBENCH_URL"); 
      }

      url = url.concat(":");

      if (System.getProperty("SUBMARINE_WORKBENCH_PORT") == null) {
        url = url.concat("8080");      
      } else {
        String port = System.getProperty("SUBMARINE_WORKBENCH_PORT");
        url = url.concat(String.valueOf(port));
      }
    } else {
      url = "http://127.0.0.1:8080";
    }

    long start = System.currentTimeMillis();
    boolean loaded = false;
    driver.get(url);

    while (System.currentTimeMillis() - start < 60 * 1000) {
      // wait for page load
      try {
        (new WebDriverWait(driver, 120)).until(new ExpectedCondition<Boolean>() {
          @Override
          public Boolean apply(WebDriver d) {
            // return d.findElement(By.tagName("div"))
            return d.findElement(By.tagName("submarine-root"))
                .isDisplayed();
          }
        });
        loaded = true;
        break;
      } catch (TimeoutException e) {
        LOG.info("Exception in WebDriverManager while WebDriverWait ", e);
        driver.navigate().to(url);
      }
    }

    if (loaded == false) {
      fail();
    }
    Dimension d = new Dimension(1920, 1080);
    driver.manage().window().setSize(d);
    return driver;
  }

  public static String getDownloadPath() {
      return downloadPath;
  }

  private static WebDriver generateWebDriver(WebDriverProvider provide) {
    if (!webDriverIsDownloaded) {
      String webDriverVersion = provide.getWebDriverVersion();
      webDriverPath = provide.downloadWebDriver(webDriverVersion);
      if (StringUtils.isNotBlank(webDriverPath)) {
        webDriverIsDownloaded = true;
      }
    }
    WebDriver driver = provide.createWebDriver(webDriverPath, downloadPath);
    return driver;
  }
}

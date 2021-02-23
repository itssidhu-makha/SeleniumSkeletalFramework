package com.org.controller;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class BrowserFactory {

    //singleton class with restricted instantiation
    private BrowserFactory(){

    }
    public static BrowserFactory getInstance(){
        if(instance==null){
            instance=new BrowserFactory();
        }
        return instance;
    }

    public static BrowserFactory instance = null;

    ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    ThreadLocal<RemoteWebDriver> remoteWebDriver = new ThreadLocal<>();

    public WebDriver getWebDriver() {
        if(Runner.remoteExecution){
         return remoteWebDriver.get();
        }else {
            return webDriver.get();
        }
    }

    public void setWebDriver(String browserName) throws MalformedURLException {

        switch (browserName) {

            case "chrome":
            MutableCapabilities chrome= new MutableCapabilities();
            if(Runner.gridDockerExecution){
                //in this mode, you will need VNC viewer to visualise your test runs
                chrome.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
                 remoteWebDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),chrome));
            }else if(Runner.sauceLab){
                String sauceUserName = System.getenv("SAUCE_USERNAME");
                String sauceAccessKey = System.getenv("SAUCE_ACCESS_KEY");
                String sauceURL = "https://ondemand.apac-southeast-1.saucelabs.com:443/wd/hub";
                MutableCapabilities capabilities = setSauceLabCaps(chrome,sauceUserName,sauceAccessKey);
                remoteWebDriver.set(new RemoteWebDriver(new URL(sauceURL),capabilities));
            }else if(Runner.zalenium){
              DesiredCapabilities desiredCapabilities=  setZaleniumCaps(browserName);
                remoteWebDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),desiredCapabilities));
            }
            else {
                //this is local infrastructure in local systems
                System.setProperty("webdriver.chrome.driver", "C://Selenium//chromedriver.exe");
                webDriver.set(new ChromeDriver());
            }

        }

    }

    private DesiredCapabilities setZaleniumCaps(String browserName) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(browserName);
        desiredCapabilities.setCapability("name", "Zalnium-Tests");
        desiredCapabilities.setCapability("build", "Build Zalenium V1");
        desiredCapabilities.setCapability("idleTimeout", 150);
        desiredCapabilities.setCapability("screenResolution", "1280x720");
        desiredCapabilities.setCapability("recordVideo", true);

        return desiredCapabilities;
    }

    private MutableCapabilities setSauceLabCaps(MutableCapabilities chrome,String sauceUserName,String sauceAccessKey) {

        chrome.setCapability("username", sauceUserName);
        chrome.setCapability("accessKey", sauceAccessKey);
        /** In order to use w3c you must set the seleniumVersion **/
        chrome.setCapability("seleniumVersion", "4.0.0-alpha-2");
        chrome.setCapability("name", "Selenium- Saucelab run");

        /**
         * in this exercise we set additional capabilities below that align with
         * testing best practices such as tags, timeouts, and build name/numbers.
         *
         * Tags are an excellent way to control and filter your test automation
         * in Sauce Analytics. Get a better view into your test automation.
         */
        List<String> tags = Arrays.asList("sauceDemo", "demoTest", "module4", "javaTest");
        chrome.setCapability("tags", tags);
        /** Another of the most important things that you can do to get started
         * is to set timeout capabilities for Sauce based on your organizations needs. For example:
         * How long is the whole test allowed to run?*/
        chrome.setCapability("maxDuration", 3600);
        /** A Selenium crash might cause a session to hang indefinitely.
         * Below is the max time allowed to wait for a Selenium command*/
        chrome.setCapability("commandTimeout", 600);
        /** How long can the browser wait for a new command */
        chrome.setCapability("idleTimeout", 1000);
        /** Setting a build name is one of the most fundamental pieces of running
         * successful test automation. Builds will gather all of your tests into a single
         * 'test suite' that you can analyze for results.
         * It's a best practice to always group your tests into builds. */
        chrome.setCapability("build", "Onboarding Sample App - Java-TestNG");

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        /** Set a second MutableCapabilities object to pass Sauce Options and Chrome Options **/
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("sauce:options", chrome);
        capabilities.setCapability("goog:chromeOptions", chromeOpts);
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("platformVersion", "Windows 10");
        capabilities.setCapability("browserVersion", "latest");
        return capabilities;
    }


}

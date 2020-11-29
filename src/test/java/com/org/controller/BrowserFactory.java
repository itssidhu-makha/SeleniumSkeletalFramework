package com.org.controller;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

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

    public WebDriver getWebDriver() {
        if(Runner.gridExecution){
         return remoteWebDriver.get();
        }else {
            return webDriver.get();
        }
    }

    public void setWebDriver(String browserName) throws MalformedURLException {

        switch (browserName) {

            case "chrome":
            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-infobars");
            options.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
            options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            options.setAcceptInsecureCerts(true);
            options.addArguments("start-maximized");//maximises the chrome window - ideal for running selenium tests
            //options.setBinary("/path/to/other/chrome/binary");//set in case your chrome installation in another drive
            options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);


            if(Runner.gridExecution){

                 remoteWebDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),options));
            }else {
                System.setProperty("webdriver.chrome.driver", "C://Selenium//chromedriver.exe");
                webDriver.set(new ChromeDriver(options));
            }

        }

    }

    ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    ThreadLocal<RemoteWebDriver> remoteWebDriver = new ThreadLocal<>();

}

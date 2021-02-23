package com.org.controller;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;


@Listeners(com.org.controller.ListenerClass.class)
public  class BaseTest {
    public static BaseTest myInstance;
    private BaseTest(){

    }
    public static BaseTest instanceBase=getInstance();

    public static BaseTest getInstance(){
        if(myInstance==null){
            myInstance= new BaseTest();
        }
        return myInstance;
    }

    BrowserFactory instance=BrowserFactory.getInstance();
    ExtentReports repo=null;
    ExtentTest test=null;

    public  ExtentTest getTest(){

        return test;
    }
    @BeforeClass
    @Parameters({"TestRunning","SheetRunning"})
    public void setReporter(@Optional("Test") String testRunnning,@Optional("Test") String sheetRunning) throws MalformedURLException {
        repo = new ExtentReports("C:\\Selenium\\reports\\"+testRunnning+"\\"+testRunnning+".html",true, NetworkMode.ONLINE);
        test =repo.startTest(testRunnning);

    }

    @BeforeTest
    public void setUpWebDriver() throws MalformedURLException {
        try {
            instance.setWebDriver("chrome");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test(description = "Dynamic test Builder",retryAnalyzer = RetryAnalyser.class)
    @Parameters({"TestRunning","SheetRunning"})
    public void initiateCases(@Optional("Test") String testRunnning,@Optional("Base") String sheetRunning){

        instance.getWebDriver().get("https://www.google.com");
        if(Runner.zalenium)
        instance.getWebDriver().manage().addCookie(new Cookie("zaleniumMessage","URL opened"));

        if("Google".equalsIgnoreCase(instance.getWebDriver().getTitle())) {
            test.log(LogStatus.PASS, "Url was launched and title matched");
        }else{
            test.log(LogStatus.FAIL, "Url was launched, but title did not match");
        }

    }

    @AfterTest
    public void releaseThreadsAndShutDown(){
        ;//This will also shut down chromedriver.exe from processes
        //removing thread locals from permGen to avoid memory overhead
        if(Runner.remoteExecution){
            instance.getWebDriver().quit();
            instance.remoteWebDriver.remove();
        }else if(Runner.sauceLab){

            instance.getWebDriver().quit();
            instance.remoteWebDriver.remove();
        }
        else{
            instance.getWebDriver().quit();
            instance.webDriver.remove();
        }

        //flush reports
        repo.endTest(test);
        repo.flush();
    }

}

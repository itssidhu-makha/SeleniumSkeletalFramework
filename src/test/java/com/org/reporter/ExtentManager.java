package com.org.reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

public class ExtentManager {

    public  ExtentTest test;

    public ExtentManager(ExtentTest test){
        this.test=test;
    }

    public ExtentReports getReportObject(){
        ExtentReports repo = new ExtentReports("C:\\Selenium\\reports"+"",false, NetworkMode.ONLINE);
        return repo;
    }
}

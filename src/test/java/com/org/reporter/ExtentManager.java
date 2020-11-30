package com.org.reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

public class ExtentManager {

    public ExtentReports getReportObject(){
        ExtentReports repo = new ExtentReports("C:\\Selenium\\reports"+"",false, NetworkMode.ONLINE);
        return repo;
    }
}

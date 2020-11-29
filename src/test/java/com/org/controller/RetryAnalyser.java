package com.org.controller;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyser implements IRetryAnalyzer {
    int retryLimit = 0;
    int counter=0;
    @Override
    public  boolean retry(ITestResult iTestResult) {


        if(counter<retryLimit){
            counter++;
            return true;
        }
        return false;
    }

}

package com.org.controller;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {

    public String suiteName;
    public String columnHeaderForTest;
    public String paramterForTestCase;
    public String paramForSheet;
    public String workbookToRun;
    public String classContainingTests;
    public static boolean gridExecution;
    public int totalThreads;

    {
        totalThreads=2;
        gridExecution=true;
        suiteName="MySuite";
        columnHeaderForTest="TestName";//This will be your column header in excel sheet
        paramterForTestCase="TestRunning";//Note this should be same as in BaseTest where your @Test annotation is located

        paramForSheet="SheetRunning";//Note this should be same as in BaseTest where your @Test annotation is located
        workbookToRun="Test";
        classContainingTests="com.org.controller.BaseTest";
    }

    public static void main(String[] args) {
        Runner runCases = new Runner();

        //In your case you will add data from excel, The below is just a sample code
        ArrayList<HashMap<String,String>> excelData = new ArrayList<>();
        HashMap<String,String> data1 = new HashMap<>();
        //data for test cases
        //test one
        data1.put(runCases.columnHeaderForTest,"TestOne");
        data1.put("Iterate","Y");

        HashMap<String,String> data2 = new HashMap<>();
        //test two
        data2.put(runCases.columnHeaderForTest,"TestTwo");
        data2.put("Iterate","Y");

        HashMap<String,String> data3 = new HashMap<>();
        //test three
        data3.put(runCases.columnHeaderForTest,"TestThree");
        data3.put("Iterate","Y");

        excelData.add(data1);
        excelData.add(data2);
        excelData.add(data3);
        runCases.testRunner(excelData);

    }
    public void testRunner(ArrayList<HashMap<String,String>> testCasesTotal){

        //run and specify parallel tests only the ones which are set to iterate as Yes
        List<HashMap<String,String>> testCaseToRun = testCasesTotal.stream()
                .filter(hashMap->hashMap.get("Iterate").equalsIgnoreCase("Y"))
                .collect(Collectors.toList());

        int totalTests = testCaseToRun.size();

        //Frame a map of parameters that needs to be passed to tests

        TestNG testng = new TestNG();

        //Set your suite file in a dynamic manner
        XmlSuite suite = new XmlSuite();
        suite.setName(suiteName);
        suite.setThreadCount(totalThreads);

        suite.setParallel(XmlSuite.ParallelMode.TESTS);

        //get all the test names from excel sheet in a list
        List<String> testNamesFromSheet = testCaseToRun.stream()
                .map(testName->testName.get(columnHeaderForTest))
                .collect(Collectors.toList());

        Map<String,String> testParams = null;
       //System.out.println(System.getProperty("java.class.path"));
        //System.out.println(com.google.common.collect.ImmutableSet.class.getResource("ImmutableSet.class"));
        //dynamically create your tests, rather than copy pasting into xml file
        //The below code will create exact number of tests which are specified as Yes in excel sheet
        for(int i=0;i<totalTests;i++){
            testParams=new HashMap<>();
            testParams.put(paramterForTestCase,testNamesFromSheet.get(i));
            testParams.put(paramForSheet,workbookToRun);

            //set your classes, where your @Test annotation are specified
            XmlClass classes = new XmlClass();
            classes.setParameters(testParams);//we are setting parameter at class level to utilise them in beforeclass annotation
            List<XmlClass> classNames = new ArrayList<>();
            classNames.add(new XmlClass(classContainingTests));

            //Set your tests here, add all the classes created above
            XmlTest test = new XmlTest(suite);
            test.setName(testNamesFromSheet.get(i));
            test.setParameters(testParams);
            test.setXmlClasses(classNames);
           // suite.addTest(test);

        }

        //finally jellify your suite at the end
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);
        testng.setXmlSuites(suites);
        testng.run();




    }

}

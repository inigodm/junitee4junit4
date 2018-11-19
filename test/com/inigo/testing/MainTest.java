package com.inigo.testing;

import com.inigo.testing.formaters.MainPageFormater;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainTest extends TestCase {

    public void testAccordion() throws FileNotFoundException {
        List<TestClass> res = buildATest();
        MainPageFormater html  = new MainPageFormater(new PrintWriter("./out.html"));
        html.format(res);
    }

    public List<TestClass> buildATest(){
        List<TestClass> res = new ArrayList<>();
        TestClass tc = new TestClass();
        tc.setName("test");
        List<TestResult> results = new ArrayList<>();
        TestResult tr = new TestResult();
        tr.setName("testmethod1");
        tr.addParamName("param1");
        tr.addParamValue("value1");
        results.add(tr);
        tr = new TestResult();
        tr.setName("testmethod2");
        tr.addParamName("param1");
        tr.addParamValue("value1");
        results.add(tr);
        tc.setResults(results);
        res.add(tc);
        tc = new TestClass();
        tc.setName("test2");
        tr = new TestResult();
        tr.setName("testmethod1");
        tr.addParamName("param1");
        tr.addParamValue("value1");
        results.add(tr);
        tr = new TestResult();
        tr.setName("testmethod2");
        tr.addParamName("param1");
        tr.addParamValue("value1");
        results.add(tr);
        tc.setResults(results);
        res.add(tc);
        return res;
    }
}

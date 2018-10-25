package com.inigo.testing.results;

import java.util.ArrayList;
import java.util.List;

public class TestClass {
    String name;
    boolean testFinished = false;
    List<TestResult> results;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<TestResult> getResults() {
	if (results == null) {
	    results = new ArrayList<TestResult>();
	}
	return results;
    }

    public void setResults(List<TestResult> results) {
	this.results = results;
    }

    public boolean isOk() {
	for (TestResult res : results) {
	    if (res.isAvaliable() && !res.isCorrect()) {
		return false;
	    }
	}
	return true;
    }

    public boolean isTestFinished() {
	return testFinished;
    }

    public void setTestFinished(boolean testFinished) {
	this.testFinished = testFinished;
    }
}

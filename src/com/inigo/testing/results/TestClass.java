package com.inigo.testing.results;

import java.util.List;

public class TestClass {
	String name;
	List<TestResult> results;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TestResult> getResults() {
		return results;
	}
	public void setResults(List<TestResult> results) {
		this.results = results;
	}
	@Override
	public String toString() {
		return "TestClass [name=" + name + ", results=\n" + results + "\n";
	}
	
	public boolean isOk(){
		for (TestResult res : results){
			if (!res.isOk()) return false;
		}
		return true;
	}
	
}

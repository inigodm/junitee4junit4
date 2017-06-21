package com.inigo.testing.results;

import java.util.ArrayList;
import java.util.List;

public class TestClass {
	String name;
	transient List<TestResult> results;
	List<TestResult> ok  = new ArrayList<>();
	List<TestResult> error  = new ArrayList<>();
	List<TestResult> ommit  = new ArrayList<>();
	
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
	public void addResult(TestResult tr){
		switch (tr.isCorrect()){
			case TestResult.OK:
				ok.add(tr);
				break;
			case TestResult.ERROR:
				error.add(tr);
				break;
			case TestResult.OMIT:
				ommit.add(tr);
		}
		
	}
	@Override
	public String toString() {
		return "TestClass [name=" + name + ", results=\n" + results + "\n";
	}
	
	public boolean isOk(){
		for (TestResult res : results){
			if (res.isCorrect() == 0){
				return false;
			}
		}
		return true;
	}
	
}

package com.inigo.testing.results;

import java.util.ArrayList;
import java.util.List;

public class TestClass {
	String name;
	List<TestResult> ok  = new ArrayList<>();
	List<TestResult> error  = new ArrayList<>();
	List<TestResult> ommit  = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return String.format("TestClass [name=%s , ok=%s, err=%s, ommit=%s \n", name, ok, error, ommit);
	}
	
	public boolean isOk(){
		return error.size() == 0;
	}
	
}

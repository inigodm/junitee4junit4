package com.inigo.testing.runners;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.finders.MethodFinder;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class ClassFinderRunner extends Runner<String>{
	ClassesFinder classFinder;
	List<String> listToRun = null;
	String mode = "basico";
	private Map parameterMap;
	
	@Override
	public List<TestClass> run(InputStream is) throws UnitTestingException {
		initListToRun(is);
		List<TestClass> res = new ArrayList<TestClass>();
		TestClass tc;
		MethodFinder methodFinder = null;
		for (String className : listToRun){			
			tc = new TestClass();
			tc.setName(className);			
			tc.setResults(getMethods(className));
			res.add(tc);
		}
		return res;
	}
	
	public List<TestResult> getMethods(String className) throws UnitTestingException{
		MethodFinder methodFinder = new MethodFinder(className);
		List<TestResult> res = new ArrayList<TestResult>();
		List<Method> methods = methodFinder.find().getExtendedTests();
		for (Method method : methods) {
			res.add(new TestResult(method));
		}
		return res;
	}
	@Override
	public void setListToRun(List<String> itemsToRun) {
		this.listToRun = itemsToRun;
	}

	public void initListToRun(InputStream is) throws UnitTestingException{
		if (listToRun == null){
			classFinder = new ClassesFinder(is);
			listToRun = classFinder.find().getBasicTests();
		}
	}
	@Override
	public void setMode(String mode) {
		this.mode = mode;		
	}

	@Override
	public void setParameters(Map parameterMap) {
		this.parameterMap = parameterMap;
	}
}

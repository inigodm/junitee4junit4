package com.inigo.testing.runners;

import java.io.BufferedReader;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.finders.MethodFinder;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class SimpleRunner implements Runner{
	MethodFinder methodFinder;
	ClassesFinder classFinder;
	BufferedReader br;
	
	public List<TestClass> run(InputStream is) throws UnitTestingException{
		classFinder = new ClassesFinder(is);
		return buildResponse();
	}
	
	private List<TestClass> buildResponse() throws UnitTestingException{
		List<String> classNames = classFinder.find().getResults();
		List<TestClass> res = new ArrayList<>();
		for (String className : classNames){
			res.add(testClass(className));
		}
		return res;
	}
	
	private TestClass testClass(String className) throws UnitTestingException{
		System.out.println("testing " + className);
		methodFinder = new MethodFinder(className);
		TestClass res = new TestClass();
		res.setName(className);
		res.setResults(calcResults());
		System.out.println(res);
		return res;
	}
	
	
	private List<TestResult> calcResults() throws UnitTestingException{
		List<TestResult> res = new ArrayList<>();
		for (Method method : methodFinder.find().getResults()){
			res.add(testMethod(method));	
		}
		return res;
	}
	
	public TestResult testMethod(Method method){
		TestResult res = new TestResult();
		res.setName(method.getName());
		try {
			Object obj = methodFinder.getClazz().newInstance();
			long time = (new Date()).getTime();
			res.setResult(method.invoke(obj));
			res.setTime((new Date()).getTime() - time);
			res.setCorrect(true);
		} catch (IllegalAccessException | IllegalArgumentException |
				InstantiationException e) {
			e.printStackTrace();
			res.setCorrect(false);
			res.setExc(e);
		} catch (InvocationTargetException err){
			res.setCorrect(false);
			res.setMsg("Error!!!!! " + err.getCause().getMessage().replaceAll("<","(").replaceAll(">",")"));
			res.setExc(err.getCause());
		}
		return res;
	}
}

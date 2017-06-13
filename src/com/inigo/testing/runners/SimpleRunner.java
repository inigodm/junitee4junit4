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
import com.inigo.testing.logging.Logger;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class SimpleRunner implements Runner{
	MethodFinder methodFinder;
	ClassesFinder classFinder;
	BufferedReader br;
	List<String> listToRun = null;
	
	public SimpleRunner(List<String> classNames){
		this.listToRun = classNames;
	}
	
	public SimpleRunner(){
		
	}
	
	public List<TestClass> run(InputStream is) throws UnitTestingException{
		initListToRun(is);
		return buildResponse();
	}
	
	private List<TestClass> buildResponse() throws UnitTestingException{
		List<TestClass> res = new ArrayList<TestClass>();
		for (String className : listToRun){
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
		List<TestResult> res = new ArrayList<TestResult>();
		for (Method method : methodFinder.find().getResults()){
			res.add(testMethod(method));	
		}
		for (Method method :  methodFinder.getTemporallyUnavaliables()){
			res.add(new TestResult(method));
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
			res.setCorrect(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			res.setCorrect(0);
			res.setExc(e);
		} catch (InvocationTargetException err){
			res.setCorrect(0);
			String msg = err.getCause() == null ? err.getMessage() : err.getCause() .getMessage();
			if (msg == null){
				msg = "null";
			}
			res.setMsg("Error!!!!! " + msg.replaceAll("<","(").replaceAll(">",")"));
			res.setExc(err.getCause());
		} catch (InstantiationException e) {
			e.printStackTrace();
			res.setCorrect(0);
			res.setExc(e);
		}
		res.setLogs(Logger.getLogs());
		return res;
	}

	@Override
	public void setListToRun(List<String> itemsToRun) {
		this.listToRun = itemsToRun;
	}
	
	public void initListToRun(InputStream is) throws UnitTestingException{
		if (listToRun == null){
			classFinder = new ClassesFinder(is);
			listToRun = classFinder.find().getResults();
		}
	}
}

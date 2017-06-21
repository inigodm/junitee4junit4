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
	List<TestClass> res;
	
	boolean stop = false;
	
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
		res = new ArrayList<TestClass>();
		for (String className : listToRun){
			if (stop){
				break;
			}
			doClassTests(className);
		}
		return res;
	}
	
	private void doClassTests(String className) throws UnitTestingException{
		System.out.println("testing " + className);
		TestClass tc = new TestClass();
		res.add(tc);
		methodFinder = new MethodFinder(className);
		tc.setName(className);
		calcResults(tc);
		System.out.println(tc);
	}
	
	
	private void calcResults(TestClass tc) throws UnitTestingException{
		for (Method method : methodFinder.find().getResults()){
			tc.addResult(testMethod(method));	
		}
		for (Method method :  methodFinder.getTemporallyUnavaliables()){
			tc.addResult(new TestResult(method));
		}
	}
	
	public TestResult testMethod(Method method){
		TestResult res = new TestResult();
		res.setName(method.getName());
		try {
			Object obj = methodFinder.getClazz().newInstance();
			long time = (new Date()).getTime();
			res.setResult(method.invoke(obj));
			res.setTime((new Date()).getTime() - time);
			res.setCorrect(TestResult.OK);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			res.setCorrect(TestResult.ERROR);
			res.setExc(e);
		} catch (InvocationTargetException err){
			res.setCorrect(TestResult.ERROR);
			String msg = err.getCause() == null ? err.getMessage() : err.getCause() .getMessage();
			if (msg == null){
				msg = "null";
			}
			res.setMsg("Error!!!!! " + msg.replaceAll("<","(").replaceAll(">",")"));
			res.setExc(err.getCause());
		} catch (InstantiationException e) {
			e.printStackTrace();
			res.setCorrect(TestResult.ERROR);
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

	@Override
	public void stop() {
		stop = true;
	}
	
	public List<TestClass> getResult(){
		return res;
	}
}

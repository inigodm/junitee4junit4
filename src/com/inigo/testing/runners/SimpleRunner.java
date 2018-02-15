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
	protected BufferedReader br;
	protected List<String> listToRun = null;
	protected String mode = "basico";
	
	public SimpleRunner(List<String> classNames){
		this.listToRun = classNames;
	}
	
	public SimpleRunner(){
		
	}
	
	public List<TestClass> run(InputStream is) throws UnitTestingException{
		initListToRun(is);
		return buildResponse();
	}
	
	protected List<TestClass> buildResponse() throws UnitTestingException{
		List<TestClass> res = new ArrayList<TestClass>();
		for (String className : listToRun){
			res.add(testClass(className, mode));
		}
		return res;
	}
	
	public static TestClass testClass(String className, String mode) throws UnitTestingException{
		System.out.println("testing " + className);
		MethodFinder methodFinder = new MethodFinder(className);
		TestClass res = new TestClass();
		res.setName(className);
		res.setResults(calcResults(methodFinder, mode));
		System.out.println(res);
		return res;
	}
	
	public static List<TestResult> calcResults(MethodFinder methodFinder, String mode) throws UnitTestingException{
		List<TestResult> res = new ArrayList<TestResult>();
		methodFinder.find();
		for (Method method :  methodFinder.getTestsForMode(mode)){
			res.add(testMethod(method, methodFinder.getClazz()));
		}
		for (Method method :  methodFinder.getTemporallyUnavaliables()){
			TestResult tr = testMethod(method, methodFinder.getClazz()).setAvaliable(false);
			tr.setAvaliable(false);
			res.add(tr);
		}
		return res;
	}
	
	private List<Method> getMethodsToTest(MethodFinder methodFinder) throws UnitTestingException{
		List<Method> methods = methodFinder.getBasicTests();
		if ("extendido".equals(mode)){
			methods = methodFinder.getExtendedTests();
		}
		return methods;
	}
	
	private static TestResult testMethod(Method method, Class clazz){
		TestResult res = new TestResult(method);
		try {
			Object obj = clazz.newInstance();
			long time = (new Date()).getTime();
			res.setReturned(method.invoke(obj));
			res.setTime((new Date()).getTime() - time);
			res.setCorrect(TestResult.CORRECT);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			res.setCorrect(TestResult.INCORRECT);
			res.setExc(e);
		} catch (InvocationTargetException err){
			res.setCorrect(TestResult.INCORRECT);
			String msg = err.getCause() == null ? err.getMessage() : err.getCause() .getMessage();
			if (msg == null){
				msg = "null";
			}
			res.setMsg("Error!!!!! " + msg.replaceAll("<","(").replaceAll(">",")"));
			res.setExc(err.getCause());
		} catch (InstantiationException e) {
			e.printStackTrace();
			res.setCorrect(TestResult.INCORRECT);
			res.setExc(e);
		}
		//res.setLogs(Logger.getLogs());
		return res;
	}

	@Override
	public void setListToRun(List<String> itemsToRun) {
		this.listToRun = itemsToRun;
	}
	
	public void initListToRun(InputStream is) throws UnitTestingException{
		if (listToRun == null){
			ClassesFinder classFinder = new ClassesFinder(is);
			listToRun = classFinder.find().getBasicTests();
		}
	}

	@Override
	public void setMode(String mode) {
		this.mode = mode;
	}
}

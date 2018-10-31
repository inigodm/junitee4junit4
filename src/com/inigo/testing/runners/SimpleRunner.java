package com.inigo.testing.runners;

import java.io.BufferedReader;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.finders.MethodFinder;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class SimpleRunner extends Runner<String> {
	protected BufferedReader br;
	protected List<String> listToRun = null;
	protected TestClass currentClass;
	protected TestResult currentMethod;
	protected Long time;

	public SimpleRunner(List<String> classNames) {
		this.listToRun = classNames;
	}

	public SimpleRunner() {

	}

	public List<TestClass> run(InputStream is) throws UnitTestingException {
		initListToRun(is);
		return buildResponse();
	}

	protected List<TestClass> buildResponse() throws UnitTestingException {
		List<TestClass> res = new ArrayList<TestClass>();
		for (String className : listToRun) {
			currentClass = new TestClass();
			res.add(currentClass);
			testClass(className);
		}
		return res;
	}

	public void testClass(String className) throws UnitTestingException {
		System.out.println("testing " + className);
		MethodFinder methodFinder = new MethodFinder(className);
		currentClass.setName(className);
		currentClass.setResults(calcResults(methodFinder));
		currentClass.setTestFinished(true);
		System.out.println(currentClass);
	}

	public List<TestResult> calcResults(MethodFinder methodFinder) throws UnitTestingException {
		List<TestResult> res = currentClass.getResults();
		methodFinder.find();
		for (Method method : methodFinder.getTestsForMode(mode)) {
			currentMethod = new TestResult(method);
			res.add(currentMethod);
			testMethod(method, methodFinder.getClazz());
		}
		for (Method method : methodFinder.getTemporallyUnavaliables()) {
			currentMethod = new TestResult(method);
			res.add(currentMethod);
			testMethod(method, methodFinder.getClazz()).setAvaliable(false);
			currentMethod.setAvaliable(false);
		}
		return res;
	}

	private TestResult testMethod(Method method, Class clazz) {
		try {
			Object obj = clazz.newInstance();
			time = (new Date()).getTime();
			currentMethod.setTime(time);
			currentMethod.setReturned(executeMethod(method, obj));
			currentMethod.setTime((new Date()).getTime() - time);
			currentMethod.setCorrect(true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			currentMethod.setCorrect(false);
			currentMethod.setExc(e);
		} catch (InvocationTargetException err) {
			currentMethod.setCorrect(false);
			String msg = err.getCause() == null ? err.getMessage() : err.getCause().getMessage();
			if (msg == null) {
				msg = "null";
			}
			currentMethod.setMsg("Error!!!!! " + msg.replaceAll("<", "(").replaceAll(">", ")"));
			currentMethod.setExc(err.getCause());
		} catch (InstantiationException e) {
			e.printStackTrace();
			currentMethod.setCorrect(false);
			currentMethod.setExc(e);
		}
		currentMethod.setTestFinished(true);
		// res.setLogs(Logger.getLogs());
		return currentMethod;
	}

	@Override
	public void setListToRun(List<String> itemsToRun) {
		this.listToRun = itemsToRun;
	}

	public void initListToRun(InputStream is) throws UnitTestingException {
		if (listToRun == null) {
			ClassesFinder classFinder = new ClassesFinder(is);
			listToRun = classFinder.find().getBasicTests();
		}
	}

	public Long getInitTime() {
		return time;
	}
}

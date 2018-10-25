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
import com.inigo.testing.finders.MethodFinder;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;


public class InstancesRunner<T> extends Runner<T> {
	protected BufferedReader br;
	protected List<T> listToRun = null;
	protected TestClass currentClass;
	protected TestResult currentMethod;

	public InstancesRunner(List<T> classNames) {
		this.listToRun = classNames;
	}

	public InstancesRunner() {

	}

	public List<TestClass> run(InputStream is) throws UnitTestingException {
		initListToRun();
		return buildResponse();
	}

	protected List<TestClass> buildResponse() throws UnitTestingException {
		List<TestClass> res = new ArrayList<TestClass>();
		for (T testCase : listToRun) {
			currentClass = new TestClass();
			res.add(currentClass);
			testClass(testCase);
		}
		return res;
	}

	public void testClass(T testCase) throws UnitTestingException {
		System.out.println("testing " + testCase.getClass().getName());
		MethodFinder methodFinder = new MethodFinder(testCase.getClass().getName());
		currentClass.setName(testCase.getClass().getName());
		currentClass.setResults(calcResults(testCase, methodFinder));
		currentClass.setTestFinished(true);
		System.out.println(currentClass);
	}

	public List<TestResult> calcResults(T testCase, MethodFinder methodFinder) throws UnitTestingException {
		List<TestResult> res = currentClass.getResults();
		methodFinder.find();
		for (Method method : methodFinder.getTestsForMode(mode)) {
			currentMethod = new TestResult(method);
			res.add(currentMethod);
			testMethod(testCase, method);
		}
		for (Method method : methodFinder.getTemporallyUnavaliables()) {
			currentMethod = new TestResult(method);
			res.add(currentMethod);
			testMethod(testCase, method).setAvaliable(false);
			currentMethod.setAvaliable(false);
		}
		return res;
	}

	private TestResult testMethod(T testCase, Method method) {
		try {
			long time = (new Date()).getTime();
			currentMethod.setReturned(executeMethod(method, testCase));
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
		} 
		currentMethod.setTestFinished(true);
		// res.setLogs(Logger.getLogs());
		return currentMethod;
	}

	@Override
	public void setListToRun(List<T> itemsToRun) {
		this.listToRun = itemsToRun;
	}

	public void initListToRun() throws UnitTestingException {
		if (listToRun == null) {
			listToRun = new ArrayList<T>();
		}
	}
}

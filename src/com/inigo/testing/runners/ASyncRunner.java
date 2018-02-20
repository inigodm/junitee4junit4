package com.inigo.testing.runners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.results.TestClass;

public class ASyncRunner extends SimpleRunner {
	List<TestClass> res = new ArrayList<TestClass>();
	Thread t = null;
	int i = 0;
	boolean running;
	ClassesFinder classFinder;
	private List<String> listToRun;

	public ASyncRunner() {
	}

	@Override
	public void setListToRun(List<String> itemsToRun) {
		this.listToRun = itemsToRun;
	}

	@Override
	public List<TestClass> run(InputStream is) throws UnitTestingException {
		if (!running) {
			runService(is);
		}
		return res;
	}

	private synchronized void runService(InputStream is) throws UnitTestingException {
		if (!running) {
			initListToRun(is);
			runInBackGround();
		}
		running = true;
	}

	private void runInBackGround() throws UnitTestingException {
		if (t != null && t.isAlive()) {
			return;
		}
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					buildResponse();
				} catch (UnitTestingException e) {
					e.printStackTrace();
				}
				running = false;
			}
		});
		t.start();
	}

	protected List<TestClass> buildResponse() throws UnitTestingException {
		for (String className : listToRun) {
			currentClass = new TestClass();
			res.add(currentClass);
			testClass(className, mode);
		}
		return res;
	}

	public void initListToRun(InputStream is) throws UnitTestingException {
		if (listToRun == null) {
			classFinder = new ClassesFinder(is);
			listToRun = classFinder.find().getBasicTests();
		}
	}

	@Override
	public void setMode(String mode) {

	}

	public List<TestClass> getResults() {
		List<TestClass> res2 = new ArrayList<TestClass>();
		synchronized (res) {
			for (TestClass tc : res) {
				res2.add(tc);
			}
			for (TestClass tc : res2) {
				if (tc.isTestFinished()) {
					res.remove(tc);
				}
			}
		}
		return res2;
	}

	public boolean isFinished() {
		return !(running && t != null && t.isAlive());
	}
}

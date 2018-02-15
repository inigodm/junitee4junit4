package com.inigo.testing.runners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class ASyncRunner  implements Runner{
	List<TestClass> res = new ArrayList<TestClass>();
	Thread t = null;
	int i = 0;
	boolean running;
	ClassesFinder classFinder;
	private List<String>  listToRun;
	
	public ASyncRunner(){
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

	private synchronized void runService(InputStream is)
			throws UnitTestingException {
		if (!running) {
			initListToRun(is);
			runInBackGround();
		}
		running = true;
	}
	
	private void runInBackGround() throws UnitTestingException {
		if (t != null && t.isAlive()){
			return;
		}
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					buildResponse(res);
				} catch (UnitTestingException e) {
					e.printStackTrace();
				}
				running = false;
			}
		});
		t.start();
	}
	
	private List<TestClass> buildResponse(List<TestClass> res) throws UnitTestingException{
		for (String className : listToRun){
			res.add(SimpleRunner.testClass(className, "extendido"));
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
	
	public List<TestClass> getResults(){
		List<TestClass> res2 = new ArrayList<TestClass>();
		synchronized (res) {
			for (TestClass tc : res){
				res2.add(tc);
			}
			res.clear();
		}
		return res2;	
	}

	public boolean isFinished() {
		return !(running && t !=  null && t.isAlive());
	}
}

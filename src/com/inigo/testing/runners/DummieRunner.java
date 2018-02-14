package com.inigo.testing.runners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Finishings;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.finders.MethodFinder;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class DummieRunner  implements Runner{
	List<TestClass> res = new ArrayList<TestClass>();
	Thread t = null;
	int i = 0;
	boolean running;
	ClassesFinder classFinder;
	List<TestClass> resAux = new ArrayList<TestClass>();
	private List<String>  listToRun;
	
	public DummieRunner(){
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
				for (String className : listToRun) {
					try {
						buildResponse(res);
					} catch (UnitTestingException e) {
						e.printStackTrace();
					}
				}
				running = false;
			}
		});
		t.start();
	}
	
	private List<TestClass> buildResponse(List<TestClass> res) throws UnitTestingException{
		for (String className : listToRun){
			res.add(SimpleRunner.testClass(className));
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
	
	public TestClass generateDummie(int index){
		TestClass tc = new TestClass();
		tc.setName("testClass"+index);
		TestResult tr = new TestResult("testMethod"+index, null);
		tr.setCorrect(TestResult.CORRECT);
		tr.setTime(21);
		tc.setResults(new ArrayList<TestResult>());
		tc.getResults().add(tr);
		return tc;
	}
	
	public List<TestClass> getResults(){
		List<TestClass> res2 = new ArrayList<TestClass>();
		synchronized (res) {
			for (TestClass tc : res){
				res2.add(tc);
				resAux.add(tc);
			}
			res.clear();
		}
		return res2;	
	}

	public boolean isFinished() {
		return !(running && t !=  null && t.isAlive());
	}
}

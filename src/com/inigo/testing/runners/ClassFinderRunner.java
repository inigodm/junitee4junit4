package com.inigo.testing.runners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.results.TestClass;

public class ClassFinderRunner implements Runner{
	ClassesFinder classFinder;
	List<String> listToRun = null;
	@Override
	public List<TestClass> run(InputStream is) throws UnitTestingException {
		initListToRun(is);
		List<TestClass> res = new ArrayList<TestClass>();
		TestClass tc;
		for (String className : listToRun){
			tc = new TestClass();
			tc.setName(className);
			res.add(tc);
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
			listToRun = classFinder.find().getResults();
		}
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}

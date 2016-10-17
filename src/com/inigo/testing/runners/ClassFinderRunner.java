package com.inigo.testing.runners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.finders.ClassesFinder;
import com.inigo.testing.results.TestClass;

public class ClassFinderRunner implements Runner{
	ClassesFinder classFinder;
	@Override
	public List<TestClass> run(InputStream is) throws UnitTestingException {
		classFinder = new ClassesFinder(is);
		List<String> classNames = classFinder.find().getResults();
		List<TestClass> res = new ArrayList<TestClass>();
		TestClass tc;
		for (String className : classNames){
			tc = new TestClass();
			tc.setName(className);
			res.add(tc);
		}
		return res;
	}

}

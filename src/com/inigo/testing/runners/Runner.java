package com.inigo.testing.runners;

import java.io.InputStream;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.results.TestClass;

public interface Runner {
	public void setListToRun(List<String> itemsToRun);
	public List<TestClass> run(InputStream is) throws UnitTestingException;
}

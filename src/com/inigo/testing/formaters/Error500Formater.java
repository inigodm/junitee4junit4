package com.inigo.testing.formaters;

import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;

public class Error500Formater extends Formater {

	public Error500Formater(PrintWriter pw) {
		super(pw);
	}

	@Override
	public void format(List<TestClass> tests) {
		
	}

}

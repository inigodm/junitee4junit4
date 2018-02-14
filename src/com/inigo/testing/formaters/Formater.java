package com.inigo.testing.formaters;

import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;

public abstract class Formater {
	protected PrintWriter pw;
	
	public Formater(PrintWriter pw){
		this.pw = pw;
	}
	
	public abstract void format(List<TestClass> tests);
}

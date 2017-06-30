package com.inigo.testing.formaters;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

import junit.framework.Test;

/**
 * 
 */

/**
 * @author inigo
 *
 */
public class HTMLFormater extends Formater{
	private boolean testsPassed = true;
	
	public HTMLFormater(PrintWriter pw){
		super(pw);
	}
	public void format(List<TestClass> tests) {
		try {
			System.out.println("Tests: " + tests);
			String json = generateJSON(tests);
			String html = getHTMLReplacingMaks(Thread.currentThread().getContextClassLoader().getResourceAsStream("result.html"), json);
			pw.println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String generateJSON(List<TestClass> tests) {
		Gson gson = new Gson();
		return gson.toJson(tests);
	}
}
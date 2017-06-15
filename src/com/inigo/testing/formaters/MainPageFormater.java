package com.inigo.testing.formaters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.inigo.testing.results.TestClass;

public class MainPageFormater extends Formater{
	
	public MainPageFormater(PrintWriter pw){
		super(pw);
	}
	
	public void format(List<TestClass> tests) {
		try {
			String json = generateJSON(tests);
			System.out.println("Thread.currentThread().getContextClassLoader()");
			String html = getHTMLReplacingMaks(Thread.currentThread().getContextClassLoader().getResourceAsStream("main.html"), json);
			pw.println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String generateJSON(List<TestClass> tests) {
		Gson gson = new Gson();
		List<ClazzMain> classes = new ArrayList<>();
		for (TestClass tc : tests){
			classes.add(new ClazzMain(tc.getName()));
		}
		return gson.toJson(classes);
	}
}

class ClazzMain{
	String name;

	public ClazzMain(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

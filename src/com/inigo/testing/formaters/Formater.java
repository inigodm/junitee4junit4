package com.inigo.testing.formaters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;

public abstract class Formater {
	protected PrintWriter pw;
	
	public Formater(PrintWriter pw){
		this.pw = pw;
	}
	
	public abstract void format(List<TestClass> tests);
	
	protected String getHTMLReplacingMaks(String filename, String replaceWith) throws IOException{
		BufferedReader r = new BufferedReader(new InputStreamReader(MainPageFormater.class.getResourceAsStream(filename), "UTF-8"));
		String s = "";
		StringBuilder sb = new StringBuilder();
		while ((s = r.readLine()) != null){
			s.replace("]][[", replaceWith);
			sb.append(s);
		}
		return sb.toString();
	}
}

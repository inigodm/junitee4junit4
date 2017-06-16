package com.inigo.testing.formaters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
	
	protected String getHTMLReplacingMaks(InputStream is, String replaceWith) throws IOException{
		BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String s = "";
		StringBuilder sb = new StringBuilder();
		System.out.println("---><---" +replaceWith);
		while ((s = r.readLine()) != null){
			s = s.replaceAll("\\]\\]\\[\\[", replaceWith);
			System.out.println("--s-" + s.indexOf("]][[") +s);
			sb.append(s);
		}
		return sb.toString();
	}
}

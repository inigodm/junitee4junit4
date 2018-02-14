package com.inigo.testing.formaters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

/**
 * 
 */

/**
 * @author inigo
 *
 */
public class HTMLFormaterAsync extends HTMLFormater{
	private boolean testsPassed = true;
	private boolean finished;
	
	public HTMLFormaterAsync(PrintWriter pw, boolean finished){
		super(pw);
	}

	public void format(List<TestClass> tests){
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang='en'>");
		pw.println("<head>");
		pw.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>");
		pw.println("</head>");
		pw.println("<body>");
		pw.println("<div id=\"tables\">");
		for (TestClass tc : tests){
			printClasses(tc);
		}
		pw.println("</div>");
		pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>");
		pw.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js' integrity='sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS' crossorigin='anonymous'></script>");
		pw.println("<script src='./junitee4junit4.js'></script>");
		pw.println("<script>" + readJS() + "</script>");
		//pw.println("<script>relaunch();</script>");
		pw.println("</body>");
		pw.println("</html>");
	}
	
	public String readJS(){
	    InputStream in = getClass().getResourceAsStream("/junitee4junit4.js"); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder builder = new StringBuilder();
	    char[] buffer = new char[8192];
	    int read;
	    try {
		while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
		    builder.append(buffer, 0, read);
		}
	    } catch (IOException e) {
		e.printStackTrace();
		builder.append("ERROR!!!!!!!!!!:").append(e.getMessage());
	    }
	    return builder.toString();
	}
}

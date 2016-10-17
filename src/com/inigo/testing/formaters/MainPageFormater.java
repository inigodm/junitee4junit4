package com.inigo.testing.formaters;

import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;

public class MainPageFormater extends Formater{
	
	public MainPageFormater(PrintWriter pw){
		super(pw);
	}
	
	public void format(List<TestClass> tests){
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang='en'>");
		pw.println("<head>");
		pw.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>");
		pw.println("</head>");
		pw.println("<body>");
		pw.println("<div>");
		pw.println("<form action='./test' method='post'>");
		for (TestClass tc : tests){
			printClasses(tc);
		}
		pw.println("</div>");
		pw.println("<div>");
		pw.println("<input type='submit' class='btn' value='Run selected' />");
		pw.println("</form>");
		pw.println("<button onclick='javascript:window.location.href=\"./test\";' class='btn'>Run all</button>");
		pw.println("</div>");
		pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>");
		pw.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js' integrity='sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS' crossorigin='anonymous'></script>");
		pw.println("</body>");
		pw.println("</html>");
	}
	
	protected void printClasses(TestClass tc) {
		pw.println("<table class='table table-striped' style='background-color:cyan; margin-bottom: 0px'>");
		pw.println("<tbody>");
		pw.println("<td style='background-color:#f0f0f0'>");
		pw.println("<input type='checkbox' name='" + tc.getName() + "' id='" + tc.getName() + "'/>");
		pw.println("<label for='" + tc.getName() + "'>"+tc.getName()+"</label>");
		pw.println("</td>");
		pw.println("</tbody></table>");
	}
}

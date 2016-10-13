package com.inigo.testing.formaters;
import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

import junit.framework.TestCase;

/**
 * 
 */

/**
 * @author inigo
 *
 */
public class HTMLFormater extends Formater{
	
	public HTMLFormater(PrintWriter pw){
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
		for (TestClass tc : tests){
			printClasses(tc);
		}
		pw.println("</div>");
		pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>");
		pw.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js' integrity='sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS' crossorigin='anonymous'></script>");
		pw.println("</body>");
		pw.println("</html>");
	}
	
	protected void printClasses(TestClass tc) {
		pw.println("<table class='table table-striped' style='background-color:" + (tc.isOk() ? "green" : "red") + "'>");
		pw.println("<thead><tr><th>Class " + tc.getName() + "<th></tr></thead>");
		pw.println("</thead>");
		pw.println("<tbody>");
		for (int i = 0; i < tc.getResults().size(); i++){
			pw.println("<tr>");
			printMethod(i, tc.getResults().get(i));
			pw.println("</tr>");
		}
		pw.println("</tbody></table>");
	}
	
	private void printMethod(int index, TestResult tr){
		boolean trOk=tr.isOk();
		pw.println("<td style='background-color:" + (trOk ? "#4caf50" : "#ca6059") + "'>");
		if (!trOk || !tr.getLogs().isEmpty()){
			pw.println("<div>");
			if (!trOk){
				pw.println("<button type='button' class='btn btn-danger' data-toggle='collapse' data-target='#demo" +tr.getName()+ index + "'>err</button>");
			}
			if (!tr.getLogs().isEmpty()){
				pw.println("<button type='button' class='btn btn-info' data-toggle='collapse' data-target='#info" +tr.getName()+ index + "'>inf</button>");
			}
		}
		pw.println(tr.getName());
		if (!trOk){
			pw.println("</div>");
		}
		pw.println("</td>");
		
		if (!tr.isOk()){
			pw.println("<td style='background-color:#ca6059'>");
			String msg = tr.getExc().getCause() == null ? tr.getExc().getMessage() : tr.getExc().getCause() .getMessage();
			if (msg == null){
				msg = "null";
			}
			pw.println(msg.replaceAll("<","(").replaceAll(">",")"));
			pw.println("</td>");
			pw.println("<td style='background-color:#ca6059'>");
			pw.println(tr.getMsg());
			pw.println("</td>");
			pw.println("</tr><tr style='background-color:#ca6059'><td colspan='2'>");
			pw.println("<div id='demo" +tr.getName()+ index +"' class='collapse'>");
			for (StackTraceElement s : tr.getExc().getStackTrace()){
				pw.println(String.format("%s.%s (%s);<br>", s.getClassName(), s.getMethodName(), s.getLineNumber()));
			}
			pw.println("</div>");
			pw.println("<td>");
		}else{
			pw.println("<td style='background-color:#4caf50' colspan='2'>");
			pw.println(((double)(tr.getTime())) + " milisecs");
			pw.println("</td>");
		}
		printLog(index, tr);
		pw.println("</tr>");
		
			
	}
	
	public void printLog(int index, TestResult tr){
		if (!tr.getLogs().isEmpty()){
			pw.println("<tr>");
			pw.println("<td style='background-color:#cccccc' colspan='3'>");
			pw.println("<div id='info" +tr.getName()+ index +"' class='collapse' style='background-color:#cccccc'>");
			for (String log : tr.getLogs()){
				pw.println(String.format("%s<br>", log));
			}
			pw.println("</div>");
			pw.println("</td>");
			pw.println("</tr>");
		}
	}
}

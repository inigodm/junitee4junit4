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
			String json = generateJSON(tests);
			String html = getHTMLReplacingMaks(Thread.currentThread().getContextClassLoader().getResourceAsStream("result.html"), json);
			pw.println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String generateJSON(List<TestClass> tests) {
		Gson gson = new Gson();
		List<TestResultJson> classes = new ArrayList<>();
		for (TestClass tc : tests){
			classes.add(new TestResultJson(tc));
		}
		return gson.toJson(classes);
	}
}

class TestResultJson{
	String name;
	List<MethodResult> ok  = new ArrayList<>();
	List<MethodResult> error  = new ArrayList<>();
	List<MethodResult> ommit  = new ArrayList<>();
	
	public TestResultJson(TestClass tc){
		List<TestResult> trs = tc.getResults();
		name = tc.getName();
		for (TestResult tr : trs){
			buildMethodResultFrom(tr);
		}
		
	}
	
	public void buildMethodResultFrom(TestResult tr){
		switch (tr.isCorrect()) {
		case TestResult.OK:
			Ok oki = new Ok();
			oki.setName(tr.getName());
			oki.setTime(tr.getTime() + "");
			ok.add(oki);
			break;
			
		case TestResult.ERROR:
			Error err = new Error();
			err.setName(tr.getName());
			err.setCause(tr.getMsg());
			err.setMsg(tr.getExc().getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			tr.getExc().printStackTrace(pw);
			err.setTrace(sw.toString());
			error.add(err);
			break;

		case TestResult.OMIT:
			Omitted om = new Omitted();
			om.setName(tr.getName());
			om.setReason(tr.getMsg());
			ommit.add(om);
		}
	}
}


//{"classes":{"name":"className","methods":{"ok":[{"name":"methodName1","time":2.42}],"error":[{"name":"methodName2","cause":"ExceptionException","msg":"ExceptionException", "trace":"traszatraza\ntrazatraza"},{"name":"methodName3","cause":"ExceptionException","msg":"ExceptionException", "trace":"traszatraza\ntrazatraza"}],"avoid":[{"name":"methodName3","reason":"Unavaliable"}]}}};

/*
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
	int trOk=tr.isCorrect();
	testsPassed &= trOk > 0; 
	pw.println("<td style='background-color:");
	switch (trOk){
	case 0:
		pw.println("#ca6059' >");
		break;
	case 1:
		pw.println("#4caf50' colspan='2' >");
		break;
	case 2:
		pw.println("#8181F7'>");
		break;
	}
	if (trOk == 0 || !tr.getLogs().isEmpty()){
		pw.println("<div>");
		if (trOk == 0){
			pw.println("<button type='button' class='btn btn-danger' data-toggle='collapse' data-target='#demo" +tr.getName()+ index + "'>err</button>");
		}
		if (!tr.getLogs().isEmpty()){
			pw.println("<button type='button' class='btn btn-info' data-toggle='collapse' data-target='#info" +tr.getName()+ index + "'>inf</button>");
		}
	}
	pw.println(tr.getName());
	if (trOk == 0){
		pw.println("</div>");
	}
	else if  (trOk == 2){
		pw.println("<td style='background-color:#8181F7'>"+tr.getMsg()+"</td>");
	}
	pw.println("</td>");
	
	if (trOk == 0){
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
		pw.println("<td style='background-color:" + (trOk == 1? "#4caf50'>" : "#8181F7'>"));
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

public boolean isTestsPassed() {
	return testsPassed;
}

public void setTestsPassed(boolean testsPassed) {
	this.testsPassed = testsPassed;
}
}*/

interface MethodResult{
	public static enum TYPES {ok, error, ommited};
	public TYPES getType();
}

class Ok implements MethodResult{
	TYPES type = MethodResult.TYPES.ok;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	String name;
	String time;
	@Override
	public TYPES getType() {
		return type;
	}
}

class Omitted implements MethodResult{
	TYPES type = MethodResult.TYPES.ommited;
	String name;
	String reason;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public TYPES getType() {
		return type;
	}
}

class Error implements MethodResult{
	TYPES type = MethodResult.TYPES.error;
	String name;
	String cause;
	String msg;
	String trace;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	@Override
	public TYPES getType() {
		return type;
	}
}

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
		/*
		List<TestResultJson> classes = new ArrayList<>();
		for (TestClass tc : tests){
			System.out.println(tc);
			classes.add(new TestResultJson(tc));
		}*/
		return gson.toJson(tests);
	}
}

class TestResultJson{
	String name;
	List<MethodResult> ok  = new ArrayList<>();
	List<MethodResult> error  = new ArrayList<>();
	List<MethodResult> ommit  = new ArrayList<>();
	
	public TestResultJson(TestClass tc){
	/*	List<TestResult> trs = tc.getResults();
		name = tc.getName();
		for (TestResult tr : trs){
			buildMethodResultFrom(tr);
		}
		*/
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

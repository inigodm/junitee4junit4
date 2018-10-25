package com.inigo.testing.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.annotations.TemporallyUntestable;

public class TestResult {
	String name;
	Object returned = null;
	String msg;
	boolean isAvaliable = true;
	volatile Throwable exc;
	String exception;
	String reason = null;
	long time;
	boolean isCorrect = true;
	boolean testFinished = false;
	List<String> params = new ArrayList<String>();
	
	public TestResult(Method method2) {
		name = method2.getName();
		for (Class<?> type : method2.getParameterTypes()) {
			params.add(type.getName());
		}
		TemporallyUntestable ann = method2.getAnnotation(TemporallyUntestable.class);
		if (ann != null){
			reason = ann.reason();
		}
		time = 0;
	}

	public Object getReturned() {
		return returned;
	}
	public void setReturned(Object result) {
		this.returned = result;
	}
	public boolean isCorrect(){
		return isCorrect;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Throwable getExc() {
		return exc;
	}
	public void setExc(Throwable exc) {
		this.exc = exc;
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement s : exc.getStackTrace()){
			sb.append(String.format("%s.%s (%s);<br>", s.getClassName(), s.getMethodName(), s.getLineNumber()));
		}
		exception = sb.toString();
	}
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		System.out.println("time is " + time);
		this.time = time;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public boolean isAvaliable() {
		return isAvaliable;
	}

	public TestResult setAvaliable(boolean isAvaliable) {
		this.isAvaliable = isAvaliable;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isTestFinished() {
	    return testFinished;
	}

	public void setTestFinished(boolean testFinished) {
	    this.testFinished = testFinished;
	}

	public List<String> getParams() {
		return params;
	}
	
	public void addParams(String name) {
		params.add(name);
	}
}

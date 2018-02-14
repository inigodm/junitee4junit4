package com.inigo.testing.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.annotations.TemporallyUntestable;

public class TestResult {
	public static final int CORRECT = 1;
	public static final int INCORRECT = 2;
	public static final int UNAVAILABLE = 3;
	String name;
	Object result;
	String msg;
	volatile Throwable exc;
	String exception;
	List<String> logs = new ArrayList<String>();
	
	long time;
	
	int isCorrect = 2;
	
	public TestResult(Method method2) {
		//method = method2;
		name = method2.getName();
		result = null;
		TemporallyUntestable ann = method2.getAnnotation(TemporallyUntestable.class);
		if (ann != null){
			msg = ann.reason();
		}
		time = 0;
		isCorrect = 2;
	}
	
	public TestResult(String name, String reason) {
		this.name = name;
		result = null;
		msg = reason;
		time = 0;
		isCorrect = 2;
	}
	public TestResult() {
		// TODO Auto-generated constructor stub
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public boolean isCorrefct() {
		return isCorrect == 1;
	}
	public int  isCorrect(){
		return isCorrect;
	}
	public void setCorrect(int isCorrect) {
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
	public List<String> getLogs() {
		return logs;
	}
	public void setLogs(List<String> logs) {
		this.logs = logs;
	}
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
}

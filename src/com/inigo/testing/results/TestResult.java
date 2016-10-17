package com.inigo.testing.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestResult {
	Method method;
	String name;
	Object result;
	String msg;
	Throwable exc;
	List<String> logs = new ArrayList<String>();
	
	long time;
	
	boolean isCorrect;
	
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public boolean isOk() {
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
	@Override
	public String toString() {
		return "TestResult [method=" + method + ", name=" + name + ", result=" + result + ", msg=" + msg + ", exc="
				+ exc + ", isCorrect=" + isCorrect + "]\n";
	}
	
}

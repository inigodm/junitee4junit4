package com.inigo.testing.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.annotations.TemporallyUntestable;

public class TestResult {
	public static final int OK = 1;
	public static final int ERROR = 2;
	public static final int OMIT = 3;
	Method method;
	String name;
	Object result;
	String msg;
	Throwable exc;
	List<String> logs = new ArrayList<String>();
	
	long time;
	
	int isCorrect;
	
	public TestResult(Method method2) {
		method = method2;
		name = method.getName();
		result = null;
		TemporallyUntestable ann = method2.getAnnotation(TemporallyUntestable.class);
		if (ann != null){
			msg = ann.reason();
		}
		time = 0;
		isCorrect = TestResult.OMIT;
	}
	public TestResult() {
		// TODO Auto-generated constructor stub
	}
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

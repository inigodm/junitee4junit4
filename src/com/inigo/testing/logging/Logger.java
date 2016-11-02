package com.inigo.testing.logging;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	static List<String> arr = new ArrayList<String>();
	
	public static void log(String log){
		StackTraceElement elem = Thread.currentThread().getStackTrace()[2];
		arr.add(String.format("%s.%s[%s]:%s",elem.getClassName(), elem.getMethodName(), elem.getLineNumber(), log));
	}
	
	public static List<String> getLogs(){
		List<String> aux = arr;
		arr = new ArrayList<String>();
		return aux;
	}
}

package com.inigo.testing.logging;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	static List<String> arr = new ArrayList<>();
	
	public static void log(String log){
		StackTraceElement elem = Thread.currentThread().getStackTrace()[1];
		arr.add(String.format("%s.%s[%s]:%s",elem.getClassName(), elem.getMethodName(), elem.getLineNumber(), log));
	}
	
	public static List<String> getLogs(){
		List<String> aux = arr;
		arr = new ArrayList<>();
		return aux;
	}
}

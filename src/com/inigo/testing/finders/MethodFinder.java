package com.inigo.testing.finders;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;

public class MethodFinder implements Finder<Method>{
	
	String classPath;
	List<Method> methods = new ArrayList<>();
	Class<?> clazz;
	
	public MethodFinder(String classPath) throws UnitTestingException{
		this.classPath = classPath;
		methods = new ArrayList<Method>();
	}

	public Finder<Method> find() throws UnitTestingException {
		try {
			clazz = this.getClass().getClassLoader().loadClass(classPath);
			//clazz = Class.forName(classPath);
			for (Method method : clazz.getDeclaredMethods()){
				if (method.getName().startsWith("test")){
					methods.add(method);
				}
			}
		} catch (SecurityException | ClassNotFoundException e) {
			throw new UnitTestingException(e);
		}
		return this;
	}

	public void setClassPath(String classPath) throws UnitTestingException {
		this.classPath = classPath;
		methods.clear();
	}

	public List<Method> getResults() {
		return methods;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}

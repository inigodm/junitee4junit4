package com.inigo.testing.finders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.annotations.TemporallyUntestable;
import com.inigo.testing.exceptions.UnitTestingException;

public class MethodFinder implements Finder<Method>{
	
	String classPath;
	List<Method> methods = new ArrayList<Method>();
	List<Method> untestables = new ArrayList<Method>();
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
					if (isTemporallyUntestable(method.getAnnotations())){
						untestables.add(method);
					}else{
						methods.add(method);
					}
				}
			}
		} catch (SecurityException  e) {
			throw new UnitTestingException(e);
		} catch (ClassNotFoundException e) {
			throw new UnitTestingException(e);
		}
		return this;
	}
	
	private boolean isTemporallyUntestable(Annotation[] anns){
		if (anns == null){
			return true;
		}
		for (int i = 0; i < anns.length; i++){
			if (anns[i].annotationType().equals(TemporallyUntestable.class)){
				return true;
			}
		}
		return false;
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

	public List<Method> getTemporallyUnavaliables() {
		return untestables;
	}

}

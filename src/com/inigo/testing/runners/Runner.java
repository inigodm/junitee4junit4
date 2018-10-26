package com.inigo.testing.runners;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.results.TestClass;

public abstract class Runner<T> {
	protected Map<String, String[]> parameterMap;
	protected String mode = "basico";
	public abstract void setListToRun(List<T> itemsToRun);
	public abstract List<TestClass> run(InputStream is) throws UnitTestingException;
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public void setParameters(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
	}
	
	public Object executeMethod(Method method, Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String[] objs = findMethodParams(method, obj.getClass());
		System.out.println(method.getName());
		System.out.println(objs);
		if (objs.length == 0) {
			return method.invoke(obj);
		}else {
			return method.invoke(obj, (Object[])objs);
		}
	}

	private String[] findMethodParams(Method method, Class<?> clazz) {
		List<String> objs = new ArrayList<String>();
		List<String> parameterNames = new ArrayList<String>(parameterMap.keySet());
		List<String> params = new ArrayList<String>();
		for (String parameter : parameterNames) {
			if (parameter.startsWith(clazz.getName() + "." + method.getName() + ".")) {
				params.add(parameter);
			}
		}
		Class<?>[] types = method.getParameterTypes();
		for (int i = 0; i < params.size(); i++) {
			objs.add(obtainParam(parameterMap.get(clazz.getName() + "." + method.getName() + "." + i),
					types[i]));
		}
		String[] res = new String[objs.size()];
		res = objs.toArray(res);
		return res;
	}
	
	private String obtainParam(String[] object, Class<?> clazz) {
		return object[0];
	}
}

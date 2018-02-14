package com.inigo.testing.finders;

import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;

public interface Finder<T> {
	public Finder<T> find() throws UnitTestingException;
	public List<T> getExtendedTests();
	public List<T> getBasicTests();
}

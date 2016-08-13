package com.inigo.testing.finders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.inigo.testing.exceptions.UnitTestingException;

public class ClassesFinder implements Finder<String> {
	BufferedReader br;
	
	List<String> res = new ArrayList<>();
	
	public ClassesFinder(InputStream is) throws UnitTestingException{
		 br = new BufferedReader(new InputStreamReader(is));		
	}

	@Override
	public Finder<String> find() throws UnitTestingException {
		String aux;
		try {
			while ((aux = br.readLine()) != null){
				if (!"".equals(aux)){
					res.add(aux.trim());
				}
			}
		} catch (IOException e) {
			throw new UnitTestingException(e);
		}
		return this;
	}

	@Override
	public List<String> getResults() {
		return res;
	}

}

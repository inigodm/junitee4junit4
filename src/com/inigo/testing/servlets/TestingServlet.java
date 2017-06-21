package com.inigo.testing.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.formaters.HTMLFormater;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.runners.ClassFinderRunner;
import com.inigo.testing.runners.Runner;
import com.inigo.testing.runners.SimpleRunner;

/**
 * Servlet implementation class TestingServlet
 */
//@WebServlet(urlPatterns="/test",
//			name="TestingServlet")
public class TestingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public static Class<? extends Runner> testRunner = null;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public TestingServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean retrievingData = request.getParameter("retrieveData") != null;
		HttpSession session = request.getSession(true);
		if (retrievingData){
			
		}else{
			String className = request.getParameter("class");
			if (className != null){
				executeTestForPetition(request, response);		    
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		executeTestForPetition(request, response);
	}
	
	private void executeTestForPetition(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Runner running = null;
	    ServletContext context = getServletContext();
		//boolean erroneo = true;
		try {
			List<String> execute = findClassesToTest(request, response);
			running = initTestRunner();
			running.setListToRun(execute);	
			List<TestClass> res = running.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
			HTMLFormater form = new HTMLFormater(response.getWriter());
			form.format(res);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | UnitTestingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<String> findClassesToTest(HttpServletRequest request, HttpServletResponse response) throws UnitTestingException{
		ServletContext context = getServletContext();
		Runner sr = new ClassFinderRunner();
		List<TestClass> res = sr.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
		return findClassesToTest(res,request, response);
	}
	
	private List<String> findClassesToTest(List<TestClass> res, HttpServletRequest request, HttpServletResponse response){
		List<String> execute = new ArrayList<String>();
		for (TestClass tc : res){
			System.out.println("1"+tc);
			System.out.println("2"+tc.getName());
			if (null!=request.getParameter(tc.getName()) || null!=request.getParameter("all")){
				execute.add(tc.getName());
			}
		}
		return execute;
	}
	
	private Runner initTestRunner() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		if (getServletContext().getResourceAsStream("/WEB-INF/JUnitEE4Juni4.cfg") != null){
			Properties p = new Properties();
			p.load(getServletContext().getResourceAsStream("/WEB-INF/JUnitEE4Juni4.cfg"));
			String className = p.getProperty("loadRunner");
			testRunner = (Class<Runner>) Class.forName(className);
		}else{
			testRunner = SimpleRunner.class;
		}
		return testRunner.newInstance();
	}

}

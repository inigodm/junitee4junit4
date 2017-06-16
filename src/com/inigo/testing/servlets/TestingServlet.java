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
    
	// Quite ugly, but functional: this way I can access both in tests
	public static HttpServletRequest request = null;
	public static HttpServletResponse response = null;
	public static Class<? extends Runner> testRunner = null;
	public static Runner running = null;
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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	
	private void runTests(List<String> execute, List<TestClass> res) throws InstantiationException, IllegalAccessException, UnitTestingException, IOException{
		if (running == null){
			ServletContext context = getServletContext();
			running = testRunner.newInstance();
			running.setListToRun(execute);	
			// meter esto en un hilo?¿
			res = running.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
		}
	}
	
	
	
	private Runner initTestRunner() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		if (running == null){
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
		return running;
	}

}

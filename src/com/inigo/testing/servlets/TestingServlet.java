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

/**
 * Servlet implementation class TestingServlet
 */
public class TestingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// Quite ugly, but functional: this way I can access both in tests
	public static HttpServletRequest request = null;
	public static HttpServletResponse response = null;
	public static Class<Runner> testRunner = null;
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
		System.out.println("doget");
		ServletContext context = getServletContext();
		//List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
		String mode = request.getParameter("mode");
		try {
			initTestRunner();
			Runner sr = testRunner.newInstance();
			sr.setMode(mode);
			sr.setParameters(request.getParameterMap());
			List<TestClass> res = sr.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
			HTMLFormater form = new HTMLFormater(response.getWriter());
			form.format(res);
		} catch (UnitTestingException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("dopost");
		ServletContext context = getServletContext();
		//boolean erroneo = true;
		String mode = request.getParameter("mode");
		try {
			initTestRunner();
			Runner sr = new ClassFinderRunner();
			sr.setMode(mode);
			sr.setParameters(request.getParameterMap());
			List<TestClass> res = sr.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
			List<String> execute = new ArrayList<String>();
			for (TestClass tc : res){
				if (null != request.getParameter(tc.getName())){
					execute.add(tc.getName());
				}
			}
			sr = testRunner.newInstance();
			sr.setMode(mode);
			sr.setListToRun(execute);	
			res = sr.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
			HTMLFormater form = new HTMLFormater(response.getWriter());
			form.format(res);
			//erroneo = form.isTestsPassed();
		} catch (UnitTestingException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void initTestRunner() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		if (testRunner == null){
			Properties p = new Properties();
			p.load(getServletContext().getResourceAsStream("/WEB-INF/JUnitEE4Juni4.cfg"));
			String className = p.getProperty("loadRunner");
			testRunner = (Class<Runner>) Class.forName(className);
		}
	}

}

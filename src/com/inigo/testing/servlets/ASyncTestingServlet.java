package com.inigo.testing.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.formaters.HTMLFormater;
import com.inigo.testing.formaters.HTMLFormaterAsync;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.runners.ClassFinderRunner;
import com.inigo.testing.runners.ASyncRunner;
import com.inigo.testing.runners.Runner;

/**
 * Servlet implementation class TestingServlet
 */
public class ASyncTestingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static Class<Runner> testRunner = null;
	public static ASyncRunner runner  = null;
 /**
     * @see HttpServlet#HttpServlet()
     */
    public ASyncTestingServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doget");
		ServletContext context = getServletContext();
		String mode = request.getParameter("mode");
		try {
			List<String> toExecute = findClassNamesFromFileForMode(context.getResourceAsStream("/WEB-INF/testCase.txt"), 
					mode,
					request);
			runner = new ASyncRunner();
			runner.setMode("extendido");
			runner.setListToRun(toExecute);	
			runner.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
			HTMLFormater form = new HTMLFormaterAsync(response.getWriter(), false);
			form.format(new ArrayList<TestClass>());
		} catch (UnitTestingException e) {
			e.printStackTrace();
		} 
	}
	
	private List<String> findClassNamesFromFileForMode(InputStream testCasetxt, String mode, HttpServletRequest request) throws UnitTestingException{
		List<String> execute = new ArrayList<String>();
		Runner sr = new ClassFinderRunner();
		sr.setMode(mode);
		List<TestClass> res = sr.run(testCasetxt);
		for (TestClass tc : res){
			if (null != request.getParameter(tc.getName()) || request.getParameter("all") != null){
				execute.add(tc.getName());
			}
		}
		return execute;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("startAsync") != null){
			doGet(request, response);
			return;
		}
		boolean finished = runner.isFinished();
		Gson gson = new Gson(); 
		List<TestClass> r = runner.getResults();
		String results = gson.toJson(new AsyncMessage(r, finished));
		response.getWriter().println(results);		
		response.flushBuffer();
		System.out.println(results);
	}
	
	class AsyncMessage{
		private boolean finished;
		private List<TestClass> res;

		public AsyncMessage (List<TestClass> res, boolean finished){
			this.res = res;
			this.finished = finished;			
		}
	}
}

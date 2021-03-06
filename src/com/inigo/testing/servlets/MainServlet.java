package com.inigo.testing.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.formaters.MainPageFormater;
import com.inigo.testing.results.TestClass;
import com.inigo.testing.runners.ClassFinderRunner;
import com.inigo.testing.runners.Runner;

/**
 * Servlet implementation class TestingServlet
 */
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	/**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		try {
			Runner sr = new ClassFinderRunner();
			List<TestClass> res = sr.run(context.getResourceAsStream("/WEB-INF/testCase.txt"));
			MainPageFormater form = new MainPageFormater(response.getWriter());
			form.format(res);
		} catch (UnitTestingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

package com.inigo.testing.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inigo.testing.exceptions.UnitTestingException;
import com.inigo.testing.formaters.HTMLFormater;
import com.inigo.testing.results.TestClass;
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
		this.request = request;
		this.response = response;
		ServletContext context = getServletContext();
		try {
			Runner sr = new SimpleRunner();
			List<TestClass> res = sr.run(context.getResourceAsStream("/WEB-INF/testCases"));
			HTMLFormater form = new HTMLFormater(response.getWriter());
			form.format(res);
		} catch (UnitTestingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

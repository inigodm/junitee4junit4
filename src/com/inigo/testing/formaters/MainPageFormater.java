package com.inigo.testing.formaters;

import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

public class MainPageFormater extends Formater{

	public MainPageFormater(PrintWriter pw){
		super(pw);
	}
	
	public void format(List<TestClass> tests){
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang='en'>");
		pw.println("<head>");
		pw.println("<script type=\"text/javascript\">" +
					"function changeFormExt() {" +
						" document.getElementById(\"formulario\").setAttribute(\"action\", \"./test?mode=extendido\");     " +
					"};" +
					"function asyncSelected() {" +
					" document.getElementById(\"formulario\").setAttribute(\"action\", \"./testAsync?startAsync=true&mode=extendido\");     " +
					"};" +
					"function changeFormBas() {" +
					" document.getElementById(\"formulario\").setAttribute(\"action\", \"./test?mode=basico\");     " +
					"};" +
					"</script>");
		pw.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>");
		pw.println("</head>");
		pw.println("<body>");
		pw.println("<div>");
		pw.println("<form id='formulario' action='./test?mode=basico' method='post'>");
		for (TestClass tc : tests){
			printClasses(tc);
		}
		pw.println("</div>");
		pw.println("<div>");
		pw.println("<input type='submit' id='selectBAS' class='btn' onclick='changeFormBas();' value='Run selected BASICO' />");
		pw.println("<input type='submit' id='selectEXT' class='btn' onclick='changeFormExt();' value='Run selected EXTENDIDO' />");
		pw.println("<input type='submit' id='selectASYNC' class='btn' onclick='asyncSelected();' value='Run selected async' />");
		pw.println("</form>");
		pw.println("<button onclick='javascript:window.location.href=\"./test?mode=basico&all=true\";' class='btn'>Run BASICO</button>");
		pw.println("<button onclick='javascript:window.location.href=\"./test?mode=extendido&all=true\";' class='btn'>Run EXTENDED</button>");
		pw.println("<button onclick='javascript:window.location.href=\"./testAsync?all=true\";' class='btn'>Run all async</button>");
		pw.println("</div>");
		pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>");
		pw.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js' integrity='sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS' crossorigin='anonymous'></script>");
		pw.println("</body>");
		pw.println("</html>");
	}
	
	protected void printClasses(TestClass tc) {
		boolean flag = false;
		for (TestResult tr : tc.getResults()) {
			if (!tr.getParamNames().isEmpty()) {
				flag = true;
				break;
			}
		}
		pw.println("<div class=\"panel panel-default\">");
		if (flag) {
			pw.println("<div data-toggle=\"collapse\" data-target=\"#functions" +tc.getUndotedName() +"\">\n");
			pw.println("<input type='checkbox' name='" + tc.getName() + "' id='" + tc.getName() + "'/>");
			pw.println("<label for='" + tc.getName() + "'>"+tc.getName()+"</label>");		
			pw.println("</div>");
		}else {
			pw.println("<input type='checkbox' name='" + tc.getName() + "' id='" + tc.getUndotedName() + "'/>");
			pw.println("<label for='" + tc.getName() + "'>"+tc.getName()+"</label>");		
		}
		if(flag) {
			drawAccordion(tc);
		}
		pw.println("</div>");
	}
	
	protected void drawAccordion(TestClass tc) {
    	pw.println("<div id='functions" +tc.getUndotedName() +"' class=\"collapse\" aria-expanded=\"false\">");
		for (TestResult tr : tc.getResults()) {
			if (!tr.getParamNames().isEmpty()) {
				pw.println("<div class=\\\"panel panel-default\\\">");
				pw.println("<label style='background-color:#f0f0f0'>"+tr.getName()+"</label>");
				for (int i = 0; i < tr.getParamNames().size(); i++) {
					String param = tr.getParamNames().get(i);
					String value = tr.getParamValues().get(i);
					pw.println("<label for='" + param + "'>" + param + "</label>");
					pw.println("<input type='text' name='"+tc.getName()+"."+tr.getName()+"."+i+"' id='"+tc.getName()+"."+tr.getName()+"."+i+"' value='" + value + "'/>");
				}
				pw.println("</div>");
			}
		}
		pw.println("</div>");
	}
	
	private void scriptAccordion() {
		pw.println("<script>");
		pw.println("$(document).ready(function(){");
		pw.println("$('.collapse.in').each(function(){");
		pw.println("$(this).siblings('.panel-heading').find('input').prop('checked',true);");
		pw.println("});");
		pw.println("$('.collapse').on('show.bs.collapse', function(){");
		pw.println("$(this).parent().find('input').prop('checked',true);");
		pw.println("}).on('hide.bs.collapse', function(){");
		pw.println("$(this).parent().find('input').prop('checked',false);");
		pw.println("});");
		pw.println("});");
		pw.println("</script>");
	}
}

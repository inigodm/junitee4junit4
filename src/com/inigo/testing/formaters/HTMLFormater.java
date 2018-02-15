package com.inigo.testing.formaters;
import java.io.PrintWriter;
import java.util.List;

import com.inigo.testing.results.TestClass;
import com.inigo.testing.results.TestResult;

/**
 * 
 */

/**
 * @author inigo
 *
 */
public class HTMLFormater extends Formater{
	private boolean testsPassed = true;
	
	public HTMLFormater(PrintWriter pw){
		super(pw);
	}

	public void format(List<TestClass> tests){
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang='en'>");
		pw.println("<head>");
		pw.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>");
		pw.println("</head>");
		pw.println("<body>");
		pw.println("<div>");
		for (TestClass tc : tests){
			printClass(tc);
		}
		pw.println("</div>");
		pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>");
		pw.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js' integrity='sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS' crossorigin='anonymous'></script>");
		pw.println("</body>");
		pw.println("</html>");
	}
	
	protected void printClass(TestClass tc) {
		pw.println("<table id=\"" + tc.getName() + "\" class='table table-striped' style='background-color:" + (tc.isOk() ? "green" : "red") + "'>");
		pw.println("<thead><tr><th>Class " + tc.getName() + "<th></tr></thead>");
		pw.println("</thead>");
		pw.println("<tbody>");
		for (int i = 0; i < tc.getResults().size(); i++){
			pw.println("<tr>");
			printMethod(i, tc.getResults().get(i));
			pw.println("</tr>");
		}
		pw.println("</tbody></table>");
	}
	
	private void printMethod(int index, TestResult tr){
		LogDrawer ld = logDrawerFactory(index, tr);
		ld.drawResult(tr);
	}
	
	private LogDrawer logDrawerFactory(int index, TestResult tr){
		if (!tr.isAvaliable()){
			return new UnAvailableDrawer(pw, index);
		}else if (tr.isCorrect()){
			return new OkDrawer(pw, index);
		}
		else{
			return new ErrorDrawer(pw, index);
		}
	}

	public boolean isTestsPassed() {
		return testsPassed;
	}

	public void setTestsPassed(boolean testsPassed) {
		this.testsPassed = testsPassed;
	}
}

abstract class LogDrawer{
	PrintWriter pw;
    int index;//TODO: I think that index is not needed, check it.
    protected abstract void drawFirstCell(TestResult tr);
    protected abstract void drawSecondCell(TestResult tr);
    protected abstract void drawAccordion(TestResult tr);
    
    public LogDrawer(PrintWriter pw, int index){
    	this.pw = pw;
    	this.index = index;
    }
    
    public void drawResult(TestResult tr){
    	pw.println("<tr>");
    	drawFirstCell(tr);
    	drawSecondCell(tr);
    	pw.println("</tr>");
    	drawAccordion(tr);
    }
}

class UnAvailableDrawer extends LogDrawer{
    
    public UnAvailableDrawer(PrintWriter pw, int index){
    	super(pw, index);
    }

	@Override
	protected void drawFirstCell(TestResult tr) {
		pw.println("<td style='background-color:#8181F7'>");
		if (tr.getExc() == null || tr.getExc().getStackTrace().length == 0){//TODO: this must dissapear and let only the 'else' case
			pw.println(tr.getName());
		}else{
			drawButton(tr);
		}
    	pw.println("</td>");
	}

	@Override
	protected void drawSecondCell(TestResult tr) {
		pw.println("<td style='background-color:#8181F7'>");
		pw.println("<div>" + tr.getReason() + "</div");
		pw.println("</td>");
		pw.println("<td style='background-color:#8181F7'>");
		pw.println(tr.getMsg());
		pw.println("</td>");
	}
	
	protected void drawButton(TestResult tr) {
    	pw.println("<div>");
    	pw.println("<button type='button' class='btn btn-info' data-toggle='collapse' data-target='#demo" +tr.getName()+ index + "'>info</button>");
    	pw.println(tr.getName());
		pw.println("</div>");
	}

	@Override
	protected void drawAccordion(TestResult tr) {
		if (tr.getExc() != null && tr.getExc().getStackTrace().length != 0){//TODO: erase this if, must be the only case
			pw.println("<tr style='background-color:#8181F7'><td colspan='2'>");
			pw.println("<div id='demo" +tr.getName()+ index +"' class='collapse'>");
			for (StackTraceElement s : tr.getExc().getStackTrace()){
				pw.println(String.format("%s.%s (%s);<br>", s.getClassName(), s.getMethodName(), s.getLineNumber()));
			}
			pw.println("</div>");
			pw.println("<td>");
			pw.println("</tr>");		
		}
	}
	
}


class OkDrawer extends LogDrawer{
    
    public OkDrawer(PrintWriter pw, int index){
    	super(pw, index);
    }

	@Override
	protected void drawFirstCell(TestResult tr) {
		pw.println("<td style='background-color:#4caf50' colspan='2' >");
		pw.println(tr.getName());
    	pw.println("</td>");
	}

	@Override
	protected void drawSecondCell(TestResult tr) {
		pw.println("<td style='background-color:#4caf50'>");
		pw.println(((double)(tr.getTime())) + " milisecs");
		pw.println("</td>");
	}

	@Override
	protected void drawAccordion(TestResult tr) {
		// No accordion because no logs to draw already
	}
}

class ErrorDrawer extends LogDrawer{
    
    public ErrorDrawer(PrintWriter pw, int index){
    	super(pw, index);
    }
    
    @Override
    protected void drawFirstCell(TestResult tr) {
    	pw.println("<td style='background-color:#ca6059' >");
    	drawButton(tr);
    	pw.println("</td>");
    }

    protected void drawButton(TestResult tr) {
    	pw.println("<div>");
    	pw.println("<button type='button' class='btn btn-danger' data-toggle='collapse' data-target='#demo" +tr.getName()+ index + "'>err</button>");
    	pw.println(tr.getName());
		pw.println("</div>");
	}

    @Override
    protected void drawSecondCell(TestResult tr) {
    	pw.println("<td style='background-color:#ca6059'>");
		String msg = tr.getExc().getCause() == null ? tr.getExc().getMessage() : tr.getExc().getCause() .getMessage();
		if (msg == null){
			msg = "null";
		}
		pw.println(msg.replaceAll("<","(").replaceAll(">",")"));
		pw.println("</td>");
		pw.println("<td style='background-color:#ca6059'>");
		pw.println(tr.getMsg());
		pw.println("</td>");
    }

    @Override
    protected void drawAccordion(TestResult tr) {
    	pw.println("<tr style='background-color:#ca6059'><td colspan='2'>");
		pw.println("<div id='demo" +tr.getName()+ index +"' class='collapse'>");
		for (StackTraceElement s : tr.getExc().getStackTrace()){
			pw.println(String.format("%s.%s (%s);<br>", s.getClassName(), s.getMethodName(), s.getLineNumber()));
		}
		pw.println("</div>");
		pw.println("<td>");
		pw.println("</tr>");
    }
    
}

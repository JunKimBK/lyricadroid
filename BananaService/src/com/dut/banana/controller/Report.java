package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestReportPackage;
import com.dut.banana.net.ResponseReportPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class Report
 */
@WebServlet("/Report")
public class Report extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Report() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String requestString = request.getParameter(Parameter.REQUEST);
		System.out.println("Receive RequestReportPackage and create new ResponseReportPackage");
		RequestReportPackage requestReportPackage = (RequestReportPackage) Package.parse(requestString);
		ResponseReportPackage responseReport = new ResponseReportPackage();
		/* Login */
		System.out.println("Call DataAccessBO and action Report");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (theDataAccess.isValidAccount(requestReportPackage.getUserName(), requestReportPackage.getPasswordHash())) {
			String comment = requestReportPackage.getComment();
			responseReport.setIsSuccess(true);
		} else {
			// lets login
		}
		/* ............ */
		System.out.println("Send ResponseReportPackage to client");
		response.getWriter().write(responseReport.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

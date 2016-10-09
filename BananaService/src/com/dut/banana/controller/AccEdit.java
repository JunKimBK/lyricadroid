package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestAccEditPackage;
import com.dut.banana.net.ResponseAccEditPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class AccEdit
 */
@WebServlet("/AccEdit")
public class AccEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccEdit() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestString = request.getParameter(Parameter.REQUEST);
		System.out.println("Receive RequestAccEditPackage and create new ResponseAccEditPackage");
		RequestAccEditPackage requestAccEdit = (RequestAccEditPackage) Package.parse(requestString);
		ResponseAccEditPackage responseAccEdit = new ResponseAccEditPackage();
		/* AccEdit */
		System.out.println("Call DataAccessBO and AccEdit action");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (theDataAccess.isValidAccount(requestAccEdit.getUserName(), requestAccEdit.getPasswordHash())) {
			theDataAccess.updateLyric(requestAccEdit.getLyric());
			responseAccEdit.setIsSuccess(true);
		} else {
			// Lets login
		}
		System.out.println("Send ResponseAccEditPackage to client");
		response.getWriter().write(responseAccEdit.toString());

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

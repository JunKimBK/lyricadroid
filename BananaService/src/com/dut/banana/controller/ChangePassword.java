package com.dut.banana.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestChangePasswordPackage;
import com.dut.banana.net.ResponseChangePasswordPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangePassword() {
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
		if (requestString == null)
			return;
		System.out.println("Receive RequestChangePasswordPackage and create new ResponseChangePasswordPackage");
		RequestChangePasswordPackage requestChangePassword = (RequestChangePasswordPackage) Package
				.parse(requestString);
		ResponseChangePasswordPackage responseChangePassword = new ResponseChangePasswordPackage();
		/* Change Password */
		System.out.println("Call DataAccessBO and ChangePassword");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (theDataAccess.isValidAccount(requestChangePassword.getUserName(),
				requestChangePassword.getPasswordHash())) {
			boolean status = theDataAccess.changePassword(requestChangePassword.getUserName(),
					requestChangePassword.getNewPasswordHash());
			System.out.println("Change password is " + (status ? "true" : "false"));
			responseChangePassword.setIsSuccess(status);
		} else {
			System.out.println("Account not exists!");
			responseChangePassword.setIsSuccess(false);
		}
		/* ............ */
		System.out.println("Send ResponseChangePasswordPackage to client");
		response.getWriter().write(responseChangePassword.toString());
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

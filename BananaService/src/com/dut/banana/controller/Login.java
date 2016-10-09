package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestLoginPackage;
import com.dut.banana.net.ResponseLoginPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
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
		if(requestString == null)
			return;
		System.out.println("Receive RequestLoginPackage and create new ResponseLoginPackage");
		RequestLoginPackage requestLogin = (RequestLoginPackage) Package.parse(requestString);
		ResponseLoginPackage responseLogin = new ResponseLoginPackage();
		/* Login */
		System.out.println("Call DataAccessBO and login action");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (theDataAccess.isValidAccount(requestLogin.getUserName(), requestLogin.getPasswordHash()))
			responseLogin.setIsSuccess(true);
		else
			responseLogin.setIsSuccess(false);
		System.out.println("Send ResponseLoginPackage to client");
		response.getWriter().write(responseLogin.toString());
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

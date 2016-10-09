package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestCreateAccountPackage;
import com.dut.banana.net.ResponseCreateAccountPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class CreateAcount
 */
@WebServlet("/CreateAcount")
public class CreateAcount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateAcount() {
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
		if(requestString == null)
			return;
		System.out.println("Receive RequestCreateAcountPackage and create new ResponseCreateAcountPackage");
		RequestCreateAccountPackage requestCreateAcount = (RequestCreateAccountPackage) Package.parse(requestString);
		ResponseCreateAccountPackage responseCreateAcount = new ResponseCreateAccountPackage();
		/* Create Acount */
		System.out.println("Call DataAccessBO and CreateAcount action");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (!theDataAccess.createAccount(requestCreateAcount.getUserName(), requestCreateAcount.getPasswordHash())) {
			responseCreateAcount.setIsSuccess(false);
			System.out.println("Create account is false!");
		}
		else {
			responseCreateAcount.setIsSuccess(true);
			System.out.println("Create account is true!");
		}
		/* ............ */
		response.getWriter().write(responseCreateAcount.toString());
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

package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.parameter.Parameter;
import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestAccDeletePackage;
import com.dut.banana.net.ResponseAccDeletePackage;

/**
 * Servlet implementation class AccDelete
 */
@WebServlet("/AccDelete")
public class AccDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccDelete() {
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
		System.out.println("Receive RequestAccDeletePackage and create new ResponseAccDeletePackage");
		RequestAccDeletePackage requestAccDelete = (RequestAccDeletePackage) Package.parse(requestString);
		ResponseAccDeletePackage responseAccDelete = new ResponseAccDeletePackage();
		/* Delelte Song */
		System.out.println("Get Lyric id for delete");
		int lyricId = requestAccDelete.getLyricId();
		System.out.println(" Call DataAcceessBo, AccDelete action");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (theDataAccess.isValidAccount(requestAccDelete.getUserName(), requestAccDelete.getPasswordHash())) {
			theDataAccess.removeLyric(lyricId);
			responseAccDelete.setIsSuccess(true);
		} else {

		}
		System.out.println("Send SesponseAccDeletePackage to client ");
		/* ............ */
		response.getWriter().write(responseAccDelete.toString());
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

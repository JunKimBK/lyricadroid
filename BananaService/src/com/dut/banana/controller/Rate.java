package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.Lyric;
import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestRatePackage;
import com.dut.banana.net.ResponseRatePackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class Rate
 */
@WebServlet("/Rate")
public class Rate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Rate() {
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
		System.out.println("Receive RequestRatePackage and create new ResponseRatePackage");
		RequestRatePackage requestRate = (RequestRatePackage) Package.parse(requestString);
		// create response Rate object
		ResponseRatePackage responseRate = new ResponseRatePackage();
		/* Rate */
		System.out.println("Call DataAcessBO and Rate action");
		DataAccessBO theDataAccess = new DataAccessBO();
		Lyric rateLyric = new Lyric();
		rateLyric.setRate(requestRate.getRate());
		rateLyric.setId(requestRate.getLyricId());
		if (theDataAccess.isValidAccount(requestRate.getUserName(), requestRate.getPasswordHash())) {
			theDataAccess.updateLyric(rateLyric);
			responseRate.setIsSuccess(true);
		} else {
			// lets login
		}
		/* ............ */
		System.out.println("Send ResponseRatePackage to client");
		// add responseSearch (String object) in response
		response.getWriter().write(responseRate.toString());
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

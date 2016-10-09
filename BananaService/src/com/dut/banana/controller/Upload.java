package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestUploadPackage;
import com.dut.banana.net.ResponseUploadPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Upload() {
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
		System.out.println("Receive RequestUploadPackage and create new ResponseUploadPackage");
		RequestUploadPackage requestUpload = (RequestUploadPackage) Package.parse(requestString);
		ResponseUploadPackage responseUpload = new ResponseUploadPackage();
		/* Upload */
		System.out.println("Call DataAccessBO and Upload action");
		DataAccessBO theDataAccess = new DataAccessBO();
		if (theDataAccess.isValidAccount(requestUpload.getUserName(), requestUpload.getPasswordHash())) {
			theDataAccess.addLyric(requestUpload.getLyric());
			responseUpload.setIsSuccess(true);
			System.out.println("Upload ok");
		} else {
			responseUpload.setIsSuccess(false);
			System.out.println("Upload false");
		}
		/* ............ */
		System.out.println("Send the ResponseUploadPackage to client");
		response.getWriter().write(responseUpload.toString());
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

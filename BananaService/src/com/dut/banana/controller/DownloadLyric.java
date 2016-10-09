package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.RequestDownloadLyricPackage;
import com.dut.banana.net.ResponseDownloadLyricPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class DownloadLyric
 */
@WebServlet("/DownloadLyric")
public class DownloadLyric extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadLyric() {
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
		System.out.println("Receive RequestDownloadLyricPackage and create new ResponseDownloadLyricPackage");
		// convert to request Download load Package
		RequestDownloadLyricPackage requestDownload = (RequestDownloadLyricPackage) Package.parse(requestString);
		// create response download object
		ResponseDownloadLyricPackage responseDownload = new ResponseDownloadLyricPackage();
		/* Download Lyric */
		System.out.println("Call DataAccessBO and  download lyric action");
		DataAccessBO theDataAccess = new DataAccessBO();
		int lyricID = requestDownload.getLyricId();
		responseDownload.setLyrics(theDataAccess.downloadLyric(lyricID));
		/* ............ */

		System.out.println("Send ResponseDownloadLyricPackage to client");
		// add responseDownload (String object) in response
		response.getWriter().write(responseDownload.toString());
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

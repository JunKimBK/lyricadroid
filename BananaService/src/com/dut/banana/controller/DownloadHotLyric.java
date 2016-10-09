package com.dut.banana.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.BaseLyric;
import com.dut.banana.Lyric;
import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.Package;
import com.dut.banana.net.ReponseDownloadHotLyricPackage;
import com.dut.banana.net.RequestDownloadHotLyricPackage;
import com.dut.banana.net.RequestDownloadLyricPackage;
import com.dut.banana.net.ResponseDownloadLyricPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class DownloadHotLyric
 */
@WebServlet("/DownloadHotLyric")
public class DownloadHotLyric extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadHotLyric() {
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
		System.out.println("Receive RequestDownloadHotLyricPackage and create new ResponseDownloadHotLyricPackage");
		// convert to request Download load Package
		RequestDownloadHotLyricPackage requestDownload = (RequestDownloadHotLyricPackage) Package.parse(requestString);
		// create response download object
		ReponseDownloadHotLyricPackage responseDownload = new ReponseDownloadHotLyricPackage();
		/* Download Hot Lyric */
		System.out.println("Call DataAccessBO and Download Hot Lyric action");
		DataAccessBO theDataAccess = new DataAccessBO();
		responseDownload.setmHotPlaylistLyrics(theDataAccess.downloadHotPlaylist());
		/* ............ */
		System.out.println("Send ResponsDownloadHotlyric to client");
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

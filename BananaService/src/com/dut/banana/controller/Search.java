package com.dut.banana.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dut.banana.model.bo.DataAccessBO;
import com.dut.banana.net.ResponseSearchPackage;
import com.dut.banana.net.RequestSearchPackage;
import com.dut.banana.parameter.Parameter;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
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
		System.out.println("Receive RequestSearchPackage and create new ResponseSearchPackage");
		// create response search object
		ResponseSearchPackage responseSearch = new ResponseSearchPackage();
		// convert to request Search Package
		RequestSearchPackage requestSearch = (RequestSearchPackage) RequestSearchPackage.parse(requestString);
		String songName = requestSearch.getSongName();
		String artist = requestSearch.getArtist();
		// create DataAcessBO for search lyric
		System.out.println("Call DataAcessBO and Search action");
		DataAccessBO theDataAccess = new DataAccessBO();
		// set lyric for responsesSerach , sent to client
		responseSearch.setBaseLyrics(theDataAccess.search(songName, artist));
		// add responseSearch (String object) in response
		System.out.println("Send ResponseSearchPackage to client");
		response.getWriter().write(responseSearch.toString());
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

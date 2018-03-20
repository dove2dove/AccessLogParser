/*
* @(#)ParserService.java 1.0 10/27/2017 
* Copyright (c) 2017-2018
*/

package com.ef.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ef.domain.Parserresult;
import com.ef.domain.Webserverlog;
import com.ef.repository.ParserresultRepository;
import com.ef.repository.WebserverlogRepository;

/**
 * ParserService: This is a multi-purpose service class. providing data load
 * service, request search service, and result write service.
 * 
 * @author <a href="mailto:victor.woodrow@gmail.com">Victor Woodrow</a>
 * @version 1.0
 */

@Service
public class ParserService {

	public static final String ERRORMSG = "Exception Error Message :- {}";
	private static final Logger logger = LoggerFactory.getLogger(ParserService.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

	@Autowired
	WebserverlogRepository webserverlogRepository;

	@Autowired
	ParserresultRepository parserresultRepository;

	/**
	 * load: This is the file load service. It initializes (clean up) the two
	 * tables involved in this project, reads data from the accesslog file,
	 * parse the data and write them into the Webserverlog repository.
	 * 
	 * @param <logfile>
	 *            The user provided accesslog file, including the path to get to
	 *            the file
	 * @return <boolean> If data is successfully read and written to table, it
	 *         returns a true, otherwise it returns a false.
	 * @exception <ParseException>
	 *                This catches any thrown exception due to parsing the date
	 *                from String to Date using wrong data format.
	 * @exception <IOException>
	 *                This catches any thrown exception due to text file read
	 *                and Webserverlog and Parserresult repository write.
	 */

	public boolean load(String logfile) {
		boolean loaded = false;
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(logfile))) {
			webserverlogRepository.deleteAll();
			parserresultRepository.deleteAll();
			String line;
			Webserverlog weblog;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\|");
				weblog = new Webserverlog();
				weblog.setDate(sdf.parse(fields[0].trim()));
				weblog.setIp(fields[1].trim());
				weblog.setRequest(removeQuote(fields[2].trim()));
				weblog.setStatus(fields[3].trim());
				weblog.setUser(removeQuote(fields[4].trim()));
				webserverlogRepository.save(weblog);
			}
			reader.close();
			loaded = true;
		} catch (ParseException | IOException excp) {
			logger.error(ERRORMSG, excp);
		}
		return loaded;
	}

	/**
	 * search: This generates the endDate and time for the requested search
	 * using the provided startDate and duration. It then uses the startDate and
	 * the generated endDate to search the Webserverlog repository for records
	 * that meets these criteria
	 * 
	 * @param <vstartdate>
	 *            The user provided startDate for the search
	 * @param <vduration>
	 *            The user provided period from startDate
	 * @return <List<Parserresult>> List of IPs and the number of times they
	 *         appear in the Webserverlog repository for the specified period
	 * @exception <ParseException>
	 *                This exception will be thrown as a result of issues with
	 *                parsing the user provided date in String to Date using
	 *                wrong data format.
	 */

	public List<Parserresult> search(String vstartdate, String vduration) {
		List<Parserresult> result = new ArrayList<>();
		try {
			Calendar calendar = Calendar.getInstance();
			Date startdate = sdfInput.parse(vstartdate);
			Date enddate;
			calendar.setTime(startdate);
			if (("hourly").equals(vduration)) {
				calendar.add(Calendar.HOUR_OF_DAY, 1);
				enddate = calendar.getTime();
			} else { // daily
				calendar.add(Calendar.HOUR_OF_DAY, 24);
				enddate = calendar.getTime();
			}
			result = webserverlogRepository.findIPCountbyDate(startdate, enddate);
		} catch (ParseException excep) {
			logger.error(ERRORMSG, excep);
		}
		return result;
	}

	/**
	 * write: This iterate over the result from the search of the Webserverlog
	 * repository, to identify which of the IPs crossed the threshold. IP's that
	 * cross the threshold are then printed on the console and written to the
	 * Parserresult repository.
	 * 
	 * @param <result>
	 *            The result from the search of the Webserverlog repository
	 * @param <threshold>
	 *            The set threshold of IP requests for this Search
	 */

	public void write(List<Parserresult> result, int threshold) {
		result.stream().filter(s -> s.getNoofrequest() > threshold).forEach(v -> {
			logger.info(" {}", v.getIp());
			v.setReasonforblocking("Requests > " + threshold);
			parserresultRepository.save(v);
		});
	}

	/**
	 * removeQuote :This is an utility service used for removing the quote
	 * character from the beginning and end of any string that might be quoted
	 * in the accesslog file
	 * 
	 * @param <quote>
	 *            A string that may or may not contain open and closing quote
	 * @return <String> The return value is a string that is cleaned up without
	 *         an opening and closing quote.
	 */

	private String removeQuote(String quote) {
		String result = quote;
		Character firstchar = quote.charAt(0);
		if (firstchar == '"') {
			StringBuilder sb = new StringBuilder(quote);
			sb.deleteCharAt(quote.length() - 1).deleteCharAt(0);
			//sb.deleteCharAt(0);
			result = sb.toString();
		}
		return result;
	}
}

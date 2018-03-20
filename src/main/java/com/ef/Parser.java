/*
* @(#)Parser.java 1.0 10/25/2017 
* Copyright (c) 2017-2018
*/

package com.ef;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ef.domain.Parserresult;
import com.ef.service.ParserService;

/**
 * Parser: This is the main class. The entry point for the standalone
 * application. It receives the command line arguments It validates the argument
 * tag against the set allowable tags. It also validate the argument values to
 * meet expected formats. It verifies the argument options used by the user to
 * determine if to load an accesslog file or not. It carries out a search for
 * data using the provided validated parameter, and finally calls the write
 * service if there are results to be written to console and file.
 * 
 * @author <a href="mailto:victor.woodrow@gmail.com">Victor Woodrow</a>
 * @version 1.0
 */

@SpringBootApplication
public class Parser implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Parser.class);
	private static final String STARTDATE = "startDate";
	private static final String DURATION = "duration";
	private static final String THRESHOLD = "threshold";
	private static final String ACCESSLOG = "accesslog";
	//
	private static String startDateValue;
	private static String durationValue;
	private static String threshholdValue;
	private static String accesslogValue;
	private static List<String> errParamters;
	//
	private static Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\.\\d{2}:\\d{2}:\\d{2}");

	@Autowired
	private ParserService parserService;

	public static void main(String[] args) {
		SpringApplication.run(Parser.class, args);
	}

	/**
	 * run: This overrides the CommandLineRunner run service. It accepts command
	 * line argument in the form of arrays of String. It calls the
	 * validateArguments and validateValues to validate the command line
	 * arguments. Based on the content of errParameter variable, which contains
	 * a list of any command line variable that does not meet validation
	 * requirement, decisions are made if to load log file and call the search
	 * service, or to only call the search service or do nothing.
	 * 
	 * @param <args>
	 *            The user provided argument
	 */

	@Override
	public void run(String... arg0) throws Exception {
		ParserAppArguments args = new ParserAppArguments(arg0);
		if (!args.getOptionNames().isEmpty()) {
			validateArguments(args);
			validateValues();
			if ((!errParamters.contains(ACCESSLOG)) && (!errParamters.contains(STARTDATE))
					&& (!errParamters.contains(DURATION)) && (!errParamters.contains(THRESHOLD))) {
				//logger.info("Wait...Processing in Progress!!!");
				if (accesslogValue != null) {
					parserService.load(accesslogValue);
					processSearch();
				} else {
					processSearch();
				}
			} else {
				errParamters.stream().forEach(v -> {
					logger.info("command Line Argument Error On : {}", v);
				});
			}
		} else {
			logger.info("Command Line Argument is Empty");
		}
	}

	/**
	 * processSearch: This is where the user request for information is actually
	 * processed, with the assumption that the data to fulfill this request must
	 * have been previously loaded. It calls the ParserService search service
	 * using the startDate and duration static variable. The outcome of this
	 * search determines if it will call the ParserService write service to
	 * print to console or table. With a returned value of listed item from the
	 * search service, the list is further sent to the write service to filter
	 * and print items that meets the set threshold.
	 */

	private void processSearch() {
		List<Parserresult> result = parserService.search(startDateValue, durationValue);
		if (!result.isEmpty()) {
			int threshold = Integer.parseInt(threshholdValue);
			parserService.write(result, threshold);
			logger.info("Request processed successfully");
		} else {
			logger.info("No Data Found");
		}
	}

	/**
	 * validateValues: This validates the static variables containing the
	 * command line values, to ensure the dates meets the specified date format
	 * requirement. It validates that the duration values are within the
	 * expected values of hourly or daily. It validates the threshold value to
	 * ensure it is numeric. It validates that the log file exist. Any of these
	 * values that fails validation is propagated to the parameter error List
	 * (errParameter) for further decisions.
	 */

	private static void validateValues() {
		errParamters = new ArrayList<>();
		if ((startDateValue == null) || (startDateValue.isEmpty()) || !datePattern.matcher(startDateValue).matches()) {
			errParamters.add(STARTDATE);
		}
		if ((durationValue == null) || (durationValue.isEmpty())
				|| ((!"hourly".equals(durationValue)) && (!"daily".equals(durationValue)))) {
			errParamters.add(DURATION);
		}
		if ((threshholdValue == null) || (threshholdValue.isEmpty()) || !threshholdValue.matches("[0-9]+")) {
			errParamters.add(THRESHOLD);
		}
		if ((accesslogValue != null) && (accesslogValue.isEmpty() || !Paths.get(accesslogValue).toFile().exists())) {
			errParamters.add(ACCESSLOG);
		}
	}

	/**
	 * validateArguments: This validates command line arguments. And based on
	 * defined expected arguments, command line argument values are extracted.
	 * Where the argument option does not exist, the defined static variable for
	 * such argument value is nulled.
	 * 
	 * @param <args>
	 *            The user provided argument, which has been loaded into the
	 *            ApplicationArgument beans
	 */

	private static void validateArguments(ApplicationArguments args) {
		if (args.containsOption(STARTDATE)) {
			startDateValue = args.getOptionValues(STARTDATE).get(0).trim();
		} else {
			startDateValue = null;
		}
		if (args.containsOption(DURATION)) {
			durationValue = args.getOptionValues(DURATION).get(0).trim();
		} else {
			durationValue = null;
		}
		if (args.containsOption(THRESHOLD)) {
			threshholdValue = args.getOptionValues(THRESHOLD).get(0).trim();
		} else {
			threshholdValue = null;
		}
		if (args.containsOption(ACCESSLOG)) {
			accesslogValue = args.getOptionValues(ACCESSLOG).get(0).trim();
		} else {
			accesslogValue = null;
		}
	}
}

package com.project.parser1.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.project.parser1.model.BlockedIP;
import com.project.parser1.model.BlockedIPRepository;
import com.project.parser1.model.LogEntry;
import com.project.parser1.model.LogEntryRepository;

@Controller
public class LogDataController {

	static Logger log = Logger.getLogger(LogDataController.class.getName());

	@Autowired
	LogEntryRepository entryRepository;

	@Autowired
	BlockedIPRepository blockRepository;

	/**
	 * Empties the LOG_ENTRY table and repopulates it with data from local file
	 * 
	 * @param filename
	 */
	public void reloadLog(String filename) {

		log.debug("reload file: " + filename);

		entryRepository.deleteAll();

		try (Stream<String> stream = Files.lines(Paths.get(filename))) {

			ArrayList<LogEntry> entryList = new ArrayList<LogEntry>();

			stream.forEach(line -> entryList.add(createLogEntry(line)));

			entryRepository.save(entryList);

			log.debug("loaded " + entryList.size() + " entries");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param String
	 *            line example format: 2017-01-01 00:00:11.763|192.168.234.82|"GET /
	 *            HTTP/1.1"|200|"swcd (unknown version) CFNetwork/808.2.16
	 *            Darwin/15.6.0"
	 */
	public LogEntry createLogEntry(String line) {

		String[] parsedLine = line.split("\\|");

		try {
			LogEntry e = new LogEntry();
			e.setDate(new Timestamp(LogEntry.DATE_FORMAT.parse(parsedLine[0]).getTime()));
			e.setIp(parsedLine[1]);
			e.setRequest(parsedLine[2]);
			e.setStatus(parsedLine[3]);
			e.setUserAgent(parsedLine[4]);

			return e;

		} catch (ParseException ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}

	public void blockIPsByDate(String startDateStr, String duration, String threshold) {

		Date startDate;

		try {
			//parse start date 
			startDate = LogEntry.DATE_FORMAT_CMD.parse(startDateStr);

		} catch (ParseException pe) {
			log.error("INVALID START DATE: " + startDateStr, pe);
			return;
		}

		// create end date from start and duration parameters
		int calendarField;
		if (duration.equalsIgnoreCase("hourly")) {
			calendarField = Calendar.HOUR_OF_DAY;
		} else if (duration.equalsIgnoreCase("daily")) {
			calendarField = Calendar.DAY_OF_MONTH;
		} else {
			log.error("INVALID DURATION PARAMETER: " + duration);
			return;
		}

		final Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(calendarField, 1);

		Date endDate = c.getTime();

		//parse threshold 
		long thresholdLong = Long.parseLong(threshold);

		//retrieve list of ips to add to block table
		List<String> ipsToBlock = entryRepository.findIpByThreshold(startDate, endDate, thresholdLong);

		//create block table entries
		List<BlockedIP> blockEntityList = new ArrayList<BlockedIP>();

		for (String ip : ipsToBlock) {

			BlockedIP blockedIP = new BlockedIP();

			blockedIP.setBlockedDate(new Timestamp(System.currentTimeMillis()));
			blockedIP.setComment("IP " + ip + " Blocked at " + blockedIP.getBlockedDate() + " for exceeding "
					+ threshold + " connections " + duration + " beginning at " + startDate);
			blockedIP.setIp(ip);

			blockEntityList.add(blockedIP);

		}

		//commit to block table
		blockRepository.save(blockEntityList);

		printBlockedIPsToConsole(blockEntityList);

	}

	public void printBlockedIPsToConsole(List<BlockedIP> blockedList) {
		StringBuilder message = new StringBuilder();
		for (BlockedIP i : blockedList) {
			message.append("\n " + i.getComment()); 
		}
		log.info(message);
	}

}

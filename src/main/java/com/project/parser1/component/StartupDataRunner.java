package com.project.parser1.component;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.project.parser1.controller.LogDataController;

@Component
public class StartupDataRunner implements ApplicationRunner {

	static Logger log = Logger.getLogger(StartupDataRunner.class.getName());
	
	@Autowired
	LogDataController logDataController;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		printRuntimeArgs(args);

		/*
		 * If a file is included, dump the old log table and load new data from the file
		 */
		if (args.getOptionNames().contains("filename")) {
			logDataController.reloadLog(args.getOptionValues("filename").get(0));
		}

		/*
		 * If block parameters are included at runtime, check the log data and add
		 * blocked IPs to blocked table
		 */
		if (args.getOptionNames().contains("startDate") 
				&& args.getOptionNames().contains("duration")
				&& args.getOptionNames().contains("threshold")) {
			logDataController.blockIPsByDate(args.getOptionValues("startDate").get(0),
					args.getOptionValues("duration").get(0), 
					args.getOptionValues("threshold").get(0));
		}

	}

	private void printRuntimeArgs(ApplicationArguments args) {

		for (String s : args.getNonOptionArgs()) {

			log.debug("non option arg: " + s);

		}

		for (String s : args.getOptionNames()) {
			log.debug("option name: " + s);

			for (String sa : args.getOptionValues(s)) {
				log.debug("option value: " + sa);
			}
		}
	}

}

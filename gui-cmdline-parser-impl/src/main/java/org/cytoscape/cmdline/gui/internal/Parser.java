package org.cytoscape.cmdline.gui.internal;

/*
 * #%L
 * Cytoscape GUI Command Line Parser Impl (gui-cmdline-parser-impl)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2010 - 2013 The Cytoscape Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import org.cytoscape.application.CyShutdown;
import org.cytoscape.application.CyVersion;

import java.util.Properties;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {
	private static final Logger logger = LoggerFactory.getLogger(Parser.class);

	private final String[] args;
	private final Options options;
	private final CyShutdown shutdown; 
	private final CyVersion version; 
	private final StartupConfig startupConfig; 
	private final Properties props; 

	public Parser(String[] args, CyShutdown shutdown, CyVersion version, StartupConfig startupConfig, Properties props) {
		this.args = args;
		this.shutdown = shutdown;
		this.version = version;
		this.startupConfig = startupConfig;
		this.props = props;
		options = initOptions(); 
		parseCommandLine(args);
	}

	private Options initOptions() {
		Options opt = new Options();

		opt.addOption("h", "help", false, "Print this message.");
		opt.addOption("v", "version", false, "Print the version number.");


		opt.addOption(OptionBuilder
		              .withLongOpt("session")
		              .withDescription("Load a cytoscape session (.cys) file.")
		              .withValueSeparator('\0').withArgName("file").hasArg() // only allow one session!!!
		              .create("s"));

		opt.addOption(OptionBuilder
		              .withLongOpt("network")
		              .withDescription( "Load a network file (any format).")
		              .withValueSeparator('\0').withArgName("file").hasArgs()
		              .create("N"));
		
		opt.addOption(OptionBuilder
		              .withLongOpt("table")
		              .withDescription("Load a data table file (any table format).")
		              .withValueSeparator('\0').withArgName("file").hasArgs()
		              .create("T"));

		opt.addOption(OptionBuilder
		              .withLongOpt("plugin")
		              .withDescription("Load a SIMPLIFIED plugin jar file/URL.")
		              .withValueSeparator('\0').withArgName("file").hasArgs()
		              .create("p"));

		opt.addOption(OptionBuilder
		              .withLongOpt("bundle")
		              .withDescription("Load a BUNDLE plugin jar file or URL.")
		              .withValueSeparator('\0').withArgName("file").hasArgs()
		              .create("b"));

		opt.addOption(OptionBuilder
		              .withLongOpt("props")
		              .withDescription(
		              "Load cytoscape properties file (Java properties format) or individual property: -P name=value.")
		              .withValueSeparator('\0').withArgName("file").hasArgs()
		              .create("P"));

		opt.addOption(OptionBuilder
		              .withLongOpt("vizmap")
		              .withDescription("Load vizmap properties file (Cytoscape VizMap format).")
		              .withValueSeparator('\0').withArgName("file").hasArgs()
		              .create("V"));

		return opt;
	}

	private void parseCommandLine(String[] args) {

		// try to parse the cmd line
		CommandLineParser parser = new PosixParser();
		CommandLine line = null;

		// first load the simple exit options
		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("Parsing command line failed: " + e.getMessage());
			printHelp();
			props.setProperty("tempHideWelcomeScreen","true");
			shutdown.exit(1);
			return;
		}
		
		if (line.hasOption("h")) {
			printHelp();
			shutdown.exit(0);
			props.setProperty("tempHideWelcomeScreen","true");
			return;
		}

		if (line.hasOption("v")) {
			logger.info("Cytoscape version: " + version.getVersion());
			System.out.println("Cytoscape version: " + version.getVersion());
			props.setProperty("tempHideWelcomeScreen","true");
			shutdown.exit(0);
			return;
		}

		// always load any properties specified
		if (line.hasOption("P"))
			startupConfig.setProperties(line.getOptionValues("P"));

		// always load any plugins specified
		if (line.hasOption("p"))
			startupConfig.setSimplifiedPlugins(line.getOptionValues("p"));

		// always load any bundle plugins specified
		if (line.hasOption("b"))
			startupConfig.setBundlePlugins(line.getOptionValues("b"));

		// Either load the session ...
		if (line.hasOption("s")) {
			startupConfig.setSession(line.getOptionValue("s"));

		// ... or all the rest.
		} else {
			if (line.hasOption("N"))
				startupConfig.setNetworks(line.getOptionValues("N"));

			if (line.hasOption("V"))
				startupConfig.setVizMapProps(line.getOptionValues("V"));

			if (line.hasOption("T"))
				startupConfig.setTables(line.getOptionValues("T"));
		}
	}

	private void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("cytoscape.{sh|bat} [OPTIONS]", options);
	}
}

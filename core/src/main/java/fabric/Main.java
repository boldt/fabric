/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fabric;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.tr.util.Logging;
import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;
import fabric.module.api.FabricSchemaTreeWalker;
import fabric.module.api.ModuleRegistry;
import fabric.module.dot.FabricDotGraphModule;
import fabric.wsdlschemaparser.schema.FSchema;

public class Main {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

	static {
		Logging.setLoggingDefaults();
	}

	public static void main(String[] args) throws Exception {

		// create the command line parser
		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		options.addOption("x", "xsd", true, "The XML Schema file to load");
		options.addOption("w", "wsdl", true, "The WSDL file to load");
		options.addOption("t", "target", true, "The target system to generate code for");
		options.addOption("p", "properties", true, "The properties file to configure the modules");
		options.addOption("m", "modules", true, "Comma-separated list of modules to run");
		options.addOption("v", "verbose", false, "Verbose logging output");
		options.addOption("h", "help", false, "Help output");

		File wsdlFile = null;
		File schemaFile = null;
		Workspace workspace = null;

		final ModuleRegistry registry = new ModuleRegistry();
		registry.register(new FabricDotGraphModule());

		final Properties properties = new Properties();
		for (FabricModule m : registry) {
			properties.putAll(m.getDefaultProperties());
		}

		final List<FabricSchemaTreeItemHandler> treeItemHandlers = new ArrayList<FabricSchemaTreeItemHandler>();

		// Parse the command line
		try {
			CommandLine line = parser.parse(options, args);

			// Check if verbose output should be used
			if (line.hasOption('v')) {
				Logger.getRootLogger().setLevel(Level.DEBUG);
			} else {
				Logger.getRootLogger().setLevel(Level.INFO);
			}

			// Output help and exit
			if (line.hasOption('h')) {
				usage(options, registry);
				System.exit(0);
			}

			// Load properties from file
			if (line.hasOption('p')) {
				String propertiesFile = line.getOptionValue('p');
				log.debug("Loading properties from {}", propertiesFile);
				properties.load(new FileReader(new File(propertiesFile)));
			}

			workspace = new Workspace(properties);

			// Create module instances
			if (line.hasOption('m')) {
				for (String moduleName : line.getOptionValue('m').split(",")) {
					moduleName = moduleName.trim();
					log.debug("Creating instance of module {}", moduleName);
					treeItemHandlers.add(registry.get(moduleName).getHandler(
							workspace, properties));
				}
			}

			if (line.hasOption('x') && line.hasOption('w')) {
				throw new Exception("Only one of -x or -w is allowed");
			} else if (line.hasOption('x')) {
				schemaFile = new File(line.getOptionValue('x'));
			} else if (line.hasOption('w')) {
				wsdlFile = new File(line.getOptionValue('w'));
			} else {
				throw new Exception("Supply one of -x or -w");
			}

		} catch (Exception e) {
			log.error("Invalid command line: " + e, e);
			usage(options, registry);
			System.exit(1);
		}

		if (wsdlFile != null) {
			//FWSDL wsdl = new FWSDL(wsdlFile);
		} else if (schemaFile != null) {
			FSchema schema = new FSchema(schemaFile);
			System.out.println(schema.toString());

			FabricSchemaTreeWalker tw = new FabricSchemaTreeWalker();
			for (FabricSchemaTreeItemHandler h : treeItemHandlers) {
				tw.walk(schema, h);
			}

			workspace.generate();

		}

	}

	private static void usage(Options options, ModuleRegistry registry) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(120, Main.class.getCanonicalName(), null, options, null);
		System.out.println(registry.toString());
	}
}

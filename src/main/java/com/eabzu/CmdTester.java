package com.eabzu;

import java.awt.EventQueue;
import java.io.Console;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Command sender 
 *
 */
public class CmdTester {
	private static final String APP_NAME = "Command tester";
	
	private static void runWindowed() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final MainWindow window = new MainWindow();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static RunStats runIntractive(String execFileName, PrintStream out) throws Exception {
		final RunStats stats = new RunStats();
		
		out.printf("# Date: %s\n", new Date().toString());
		
        try {
        	final Console console = System.console();
        	if (console == null)
        		throw new Exception("Application is not in interactive mode: no console");
        	
    		out.printf("# Exec: '%s'\n", execFileName);
			final ManagedProcess p = new ManagedProcess(execFileName);
			
			out.println("# Interactive mode. To quit type: 'quit' or just 'q' or 'exit'");
			
			String line = null;
			while ( (line = console.readLine("> ")) != null) {
				line = line.trim();
				
				if (line.isEmpty())
					continue;
				
				if (line.equals("q") || line.equals("quit") || line.equals("exit"))
					break;
					
				out.println(p.pipe(line));
			}
			
	        out.println("-------------");
	        out.printf("Total: %d, Ok: %d, Failed: %d\n", 
	        		stats.testRun, stats.testPassed, stats.testFailed);
	        
		} catch (IOException ex) {
			out.printf("Failed to spawn managed process['%s']: %s\n",
					execFileName,
					ex.getMessage()
					);
		}
        
        return stats;
	}

	private static RunStats runScript(String execFileName, String scriptFileName, PrintStream out) {
		final RunStats stats = new RunStats();
		
        try {
	        // Begin test run
			out.printf("# Date: %s\n", new Date().toString());
			
			out.printf("# Exec: '%s'\n", execFileName);
			final ManagedProcess p = new ManagedProcess(execFileName);
			
			out.printf("# Script: '%s'\n", scriptFileName);
			out.println();
			
	        final Script script = new Script(scriptFileName);
	        final String commentStart = script.getSyntax().COM_START;
	        for (ScriptNode test : script) {
	        	
	        	if (test != null) {
			        out.printf("%s %s\n",
			        		script.getSyntax().CMD_START,
			        		test.getCommand());
			        
			        try {
			        	stats.testRun++;
			        	final String result = p.pipe(test.getCommand());
			        	
			        	final String expected = test.getExpectedResponse();
			        	final String expectedRegex = expected.replace("\\", "\\\\")
			        			.replace("|", "\\|")
			        			.replace("{", "\\{")
			        			.replace("}", "\\}")
			        			.replace("[", "\\[")
			        			.replace("]", "\\]")
			        			;
			        	
			        	if ((result == null && (expected == null || expected.isEmpty())) ||
		        			(result != null && result.matches(expectedRegex)) ) {
			        		stats.testPassed++;

			        		out.printf("%s %s\n", script.getSyntax().RES_START, result);
				        	out.printf("%s --------------\n", commentStart);
				        	out.printf("%s Ok\n", commentStart);
			        	} else {
				        	stats.testFailed++;
					        
				        	out.printf("%s Expected: '%s'\n", commentStart, test.getExpectedResponse());
				        	out.printf("%s Received: '%s'\n", commentStart, result);
				        	out.printf("%s --------------\n", commentStart);
				        	out.printf("%s Failed\n", commentStart);
			        	}
				        
			        } catch (Exception ex) {
			        	stats.testFailed++;
				        
			        	out.printf("%s Exception: %s\n", commentStart, ex.getMessage());
			        	out.printf("%s --------------\n", commentStart);
			        	out.printf("%s Failed\n", commentStart);
			        }
			        	
		        	out.println();
	        	}
	        }

	        out.println("-------------");
	        out.printf("Total: %d, Ok: %d, Failed: %d\n", 
	        		stats.testRun, stats.testPassed, stats.testFailed);

		} catch (Exception ex) {
        	stats.testFailed++;
        	
			out.println(ex.getMessage());
//			out.println(String.format("Failed to spawn managed process['%s']: %s",
//					execFileName,
//					ex.getMessage()
//					));
		}
        
        return stats;
	}
	
	public static void main(String[] args) throws Exception {
		// Command line option parsing
		final Options appOptions = new Options();
		
		try {	// parse command line arguments
			final JCommander parser = new JCommander(appOptions, args);
			if (appOptions.isHelpRequested()) {
				parser.usage();
				return;
			}
		} catch (final ParameterException ex) { // No game this time
			System.err.println(ex.getMessage());
//			parser.usage();
			return;
		}
		
		RunStats stats = null;
		
		if (appOptions.isHeadless()) {
			final String execCommand = appOptions.getExecPath(); 
			if (execCommand == null) {
				System.err.println("Executable name expected. Please use --help to get help.");
				return;
			}
			
			final String logName = appOptions.getLogPath();
			
			if (logName != null)
				System.out.printf("%s: Redirecting output to '%s'\n", APP_NAME, logName);
	        
	        final PrintStream cps = (logName == null) 
	        				? null 
	        				: new PrintStream(logName);
	        
	        final PrintStream out = (logName == null) ? System.out : cps; 
	        try {
	        		
		        if (appOptions.isInteractive()) {
		        	runIntractive(appOptions.getExecPath(), out);
		        } else {
		        	final String scriptName = appOptions.getScript(); 
		        	if (scriptName == null) {
		    			System.err.println("For non interactive mode script name is expected. Use --help to get help.");
		        	} else {
			        	stats = runScript(	appOptions.getExecPath(),
			        						scriptName, 
			        						out);
		        	}
		        }
		        
	        } finally {
	        	if (cps != null)
	        		cps.close();
	        }
	        
		} else {
			runWindowed();
		}
		
		// System.exit is called instead of simple return to give an error code
		// Back to calling application, if any.
		System.exit( (stats != null) ? stats.testFailed : 0 );
    }
}

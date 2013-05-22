package com.eabzu;

import com.beust.jcommander.Parameter;

public class Options {
	
	@Parameter(names = {"-h", "--help"}, help = true)
	private boolean _help = false;
	
	@Parameter(names = {"-e", "--exec"}, description = "Executable to run")
	private String _execPath = null;//"DarkWars.exe";
	
	@Parameter(names = {"-s", "--script"}, description = "Script file")
	private String _script = null;//"net-log";
	
	@Parameter(names = {"-o", "--log"}, description = "Output log file")
	private String _logPath = null;

	@Parameter(names = "--headless", description = "Do NOT show GUI is this option is given", arity = 1)
	private Boolean _isHeadless = true;
	
	@Parameter(names = {"-i", "--interactive"}, description = "Run in interactive mode", arity = 1)
	private Boolean _isInteractive = true;

	public boolean isHelpRequested() { return _help; }
	
	public String getExecPath() { return _execPath; }
	public String getScript() { return _script; }
	public String getLogPath() { return _logPath; }
	
	public boolean isHeadless() { return _isHeadless; }
	public boolean isInteractive() { return _isInteractive; }
}

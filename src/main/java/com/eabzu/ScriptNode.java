package com.eabzu;

public class ScriptNode {

	public ScriptNode(String cmd, String resp) {
		setCommand(cmd);
		setResponse(resp);
	}
	
	public String getCommand() { return _cmd; }
	public void setCommand(String value) { _cmd = value; }

	public String getResponse() { return _resp; }
	public void setResponse(String value) { _resp = value; }
	
	private String _cmd;
	private String _resp;
}

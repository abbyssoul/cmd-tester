package com.eabzu;

public class ScriptNode {

	public ScriptNode(String cmd, String resp) {
		setCommand(cmd);
		setExpectedResponse(resp);
	}
	
	public String getCommand() { return _message; }
	public void setCommand(String value) { _message = value; }

	public String getExpectedResponse() { return _expectedResponce; }
	public void setExpectedResponse(String value) { _expectedResponce = value; }
	
	private String _message;
	private String _expectedResponce;
}

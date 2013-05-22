package com.eabzu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ManagedProcess {

	public ManagedProcess(String exec) throws IOException {
		_managedProc = Runtime.getRuntime().exec(exec);//new ProcessBuilder(exec).start();

		_inp = new BufferedReader(new InputStreamReader(_managedProc.getInputStream()));
		_out = new BufferedWriter(new OutputStreamWriter(_managedProc.getOutputStream()));
	}

//	public boolean test(String expect, String cmd) throws Exception {
//		final String resp = pipe(cmd);
//		if ( !resp.equals(expect))
//			throw new Exception(String.format("Test failed - expected '%s', got: '%s'",
//					expect, resp
//					));
//		
//		return true;
//	}

	public String pipe(String msg) {

		try {
			_out.write(msg + "\n");
			_out.flush();
			return _inp.readLine();
		} catch (Exception err) {
		}
		
		return "";
	}

	private final Process 			_managedProc;
	private final BufferedReader 	_inp;
	private final BufferedWriter 	_out;	
}

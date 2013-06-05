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

	public String pipe(String msg) throws IOException {
		_out.write(msg + "\n");
		_out.flush();
		
		final StringBuilder sb = new StringBuilder();
		sb.append(_inp.readLine());
		Thread.yield();
		
		while (_inp.ready()) {
			sb.append(_inp.readLine());
		}
		
		return sb.toString();
	}

	private final Process 			_managedProc;
	private final BufferedReader 	_inp;
	private final BufferedWriter 	_out;	
}

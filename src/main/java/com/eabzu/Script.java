package com.eabzu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class Script implements Iterable<ScriptNode> {
	
	public static class Syntax {
		public String CMD_START = "->";
		public String RES_START = "-<";
		public String COM_START = "#";
	}

	private final Syntax _syntax;
	private final String _fileName;
	
	public Script(String fileName) throws IOException {
		this(fileName, new Syntax());
	}

	public Script(String fileName, Syntax syntax) throws IOException {
		if (fileName == null)
			throw new IOException("Filename can not be null");
		
		if (syntax == null)
			throw new NullPointerException("Syntax can not be null");
		
		_syntax = syntax;
		_fileName = fileName;
	}
	
	public Syntax getSyntax() { return _syntax; }

	@Override
	public Iterator<ScriptNode> iterator() {
		try {
			return new ScriptNodeIterator(_fileName);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private class ScriptNodeIterator implements Iterator<ScriptNode> {
		private final BufferedReader _inputReader;
		private long _lineNumber = 1;
		private String _cmdLine = null;

//		public ScriptNodeIterator(Reader reader) {
//			_inputReader = new BufferedReader(reader);
//		}

		public ScriptNodeIterator(String fileName) throws IOException {
			_inputReader = new BufferedReader(new FileReader(fileName));
		}
		
        @Override
        public boolean hasNext() {
        	try {
            	_cmdLine = readTill(getSyntax().CMD_START);
            	return (_cmdLine != null);
            } catch (IOException e) {
                return false;
            }
        }

        private String nextLine() throws IOException {
        	_lineNumber++;
        	final String line = _inputReader.readLine(); 
        	return (line == null) ? line : line.trim();
        }
        
        private String readTill(String stopToken) throws IOException {
        	String token = null;
        	
        	while (token == null) {
        		final String line = nextLine();
        		
        		if (line == null)
        			break;
        		
        		if (line.isEmpty())
        			continue;
        		
        		if (line.startsWith(getSyntax().COM_START)) { // Skip comment line
        			continue;
        		}
        		
        		if (line.startsWith(stopToken)) {
        			token = line.substring(stopToken.length()).trim();
        		} else {
        			throw new IOException(String.format("Syntax error[%s]: Unexpected line: '%s'", _lineNumber, line));
        		}
        	}
        	
        	return token;
        }
        
        private ScriptNode readNode() throws IOException {
        	final String cmd = (_cmdLine == null) ? readTill(getSyntax().CMD_START) : _cmdLine;
        	final String resp = readTill(getSyntax().RES_START);
			return new ScriptNode(cmd, resp);
        }
        
        @Override
        public ScriptNode next() {
        	try {
                return readNode();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        	
        	return null;
        }

        @Override
        public void remove() {
        }
    };
}

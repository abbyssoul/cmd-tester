cmd-tester
==========

Much like JUnit to test process interaction.

Introduction
==========
This is a simple tool that can spawn a process and send commands to its 
standard input and compare response with expected.

Example:
- Given following 'src/test/test-script':
	> cat src/test/test-script
		# This is a comment
		-> hello
		-< hello
		
		# This is an other commented line that will result +1 failure if fed to cat
		-> world
		-< What?

- Command:
	> java -jar commandTester.jar --exec "cat" --script "src/test/test-script" 
	# Date: Thu May 23 00:26:48 EST 2013
	# Exec: 'cat'
	# Script: 'src/test/test-script'
	-> hello
	-< hello
	
	-> world
	# Test failed:
	#	Expected: What?
	#	Received: world
	
	-------------
	Total: 2, Ok: 1, Failed: 1

	... will spawn 'cat' and will send every line of script.txt that 
	start with '->' to 'cat' input and compare that it returns string 
	that start with '-<'
	
How can it be used?
==========
This tool is used for testing of text based network protocols. 
It is probably not the most optimal way of doing it, but it gets the job done:
	> java -jar commandTester.jar --exec "netcat hostname:12345" --script "script.txt"
	
	...will send content of script.txt to host 'hostname' to port 12345 via TCP 
	and	compare response with what is expected.   

Try following for list of supported options:
	> java -jar commandTester.jar --help

Author
==========
	abbyssoul aka Ivan Ryabov

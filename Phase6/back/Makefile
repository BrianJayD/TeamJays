main: comp
	java -cp .:junit-4.10.jar org.junit.runner.JUnitCore test

comp: transactionReader.java updateUser.java updateTickets.java
	javac -cp junit-4.10.jar transactionReader.java updateUser.java updateTickets.java test.java

clean:
	rm -f *.class

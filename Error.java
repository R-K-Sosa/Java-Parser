//PROJECT 2: ROHANA SOSA & TRISTAN STILL
//NOTE: Replace content of test.txt with content of Test22.txt, Test33.txt, Test44.txt, and Test55.txt to test programs


package analyzerPkg;

public class Error{
	  public Error(String message, int line){
	    System.out.println("Line " + line + ": " + message);
	    System.exit(0);
	  }
	}
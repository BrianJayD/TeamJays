import java.io.*;
import java.util.*;

class transactionReader {
  private static String fileName;


  // Constructor for transactionReader
  public transactionReader(String file) {
    fileName = file;
  }

  // Function to get file name
	public static String getFileName() {
		return fileName;
	}

  // Function to set file name
	public static void setFileName(String fileName) {
		transactionReader.fileName = fileName;
	}

  // readFile() reads the file and returns the transaction strings into an
  // array list
  public ArrayList<String> readFile() {

    ArrayList<String> trnactns = new ArrayList<String>();

    try {
      Scanner sc = new Scanner(new File(getFileName()));


      while (sc.hasNextLine()) {
        trnactns.add(sc.nextLine());
      }

      sc.close();
    } catch (IOException e) {
      System.err.println("File not found!");
      e.printStackTrace();
    }
    return trnactns;
  }

  // This function will run through the transaction array list, gets the
  // transaction number from the line and performs the corresponding update
  // function.
  public void runUpdate(ArrayList<String> tActions, String uFile, String eFile) {
    updateUser upU = new updateUser(uFile);
    updateTickets upT = new updateTickets(eFile);

    for (int i = 0; i < tActions.size(); i++) {
      String tNum = tActions.get(i).substring(0,2);
      if (tNum.equals("00")) {
        // This is a logout transaction
      } else if (tNum.equals("01")) {
        upU.createUser(tActions.get(i));
      } else if (tNum.equals("02")) {
        // This is the delete transaction
      } else if (tNum.equals("03")) {
        upT.newEvent(tActions.get(i));
      } else if (tNum.equals("04")) {
        // This is the buy transaction
        upT.buyTicket(tActions.get(i));
      } else if (tNum.equals("05")) {
        // This is the refund transaction
      } else if (tNum.equals("06")) {
        // This is the add credit transaction
      } else {
        System.out.println("Error: Transaction does not exist.");
      }
    }
  }

}

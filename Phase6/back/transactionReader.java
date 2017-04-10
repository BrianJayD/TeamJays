import java.io.*;
import java.util.*;

class transactionReader {
  private static String fileName;

  public transactionReader(String file) {
    fileName = file;
  }

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		transactionReader.fileName = fileName;
	}

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

  public String getTransNum(String t) {
    char[] tChar = t.toCharArray();
    char[] tNum = new char[2];

    for (int i = 0; i < 2; i++) {
      tNum[i] = (tChar[i]);
    }

    String strNum = new String(tNum);
    return strNum;
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
        upU.deleteUser(tActions.get(i));
      } else if (tNum.equals("03")) {
        upT.newEvent(tActions.get(i));
      } else if (tNum.equals("04")) {
        upT.buyTicket(getFileName(), uFile, tActions.get(i));
      } else if (tNum.equals("05")) {
        upU.refund(tActions.get(i));
      } else if (tNum.equals("06")) {
        upU.gainCredit(tActions.get(i));
      } else {
        System.out.println("Error: Transaction does not exist.");
      }
    }

  }



}

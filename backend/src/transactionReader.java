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

    String tnString = new String(tNum);

    return tnString;
  }

  

}

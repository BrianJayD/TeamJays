import java.io.*;
import java.util.*;
import java.lang.Double;
import java.text.DecimalFormat;

class updateUser {
  private static String accFile;
  private static DecimalFormat  df2 = new DecimalFormat("000000.00");

  public updateUser(String aFile) {
    accFile = aFile;
  }

  public updateUser() {}

  public static String getAccFile() {
    return accFile;
  }

  public static void setAccFile(String accFile) {
    updateUser.accFile = accFile;
  }

  public boolean checkUser(String uname) {
    transactionReader tRead =
      new transactionReader("current_user_accounts_file.txt");

    // Puts current users into an array list for comparing
    ArrayList<String> usrInfo = tRead.readFile();

    // Get usernames of 15 characters from the user array list and compare
    // with username passed in this function and return true if found.
    for (int i = 0; i < usrInfo.size(); i++) {
      if (uname.equals(usrInfo.get(i).substring(0, 15))) {
        return true;
      }
    }

    return false;
  }

  // Incomplete
  public void deleteUser(String duString) {
    String uname = duString.substring(3, 18);

    if(!checkUser(uname)){
      System.out.println("User does not exist");
      return;
    } else {
      updateInfo(duString.substring(0,2), uname, "  ");
    }
  }

  public void createUser(String cuString) {
    // Gets username of 15 characters from transaction string, which are
    // characters 3 to 18.
    String newUsr = cuString.substring(3, 18);

    // Performs a check to make sure that user does not exist, then writes to
    // file.
    if(checkUser(newUsr)){
      System.out.println("ERROR: username exist");
      return;
    } else {

      try{
        Writer output;
        output = new BufferedWriter(
          new FileWriter(getAccFile(), true));

        // substring(3) cuts out the first 3 characters in the transaction
        // string and appends to the file.
        output.append(cuString.substring(3) + "\n");
        output.close();
      }catch(Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  public void gainCredit(String gcString) {
    String uname = gcString.substring(3,18);
    String credit = gcString.substring(22);
    int sInd = 0;
    for (int i = 0; i < credit.length(); i++) {
      if (credit.charAt(i) == '0') {
        sInd++;
      } else if (credit.charAt(i) != 0) {
        break;
      }
    }

    String toConv = credit.substring(sInd);
    Double toAcc = Double.parseDouble(toConv);

    //Now get users current credit
    String uInfo = userInfo(uname);
    Double inAcc = Double.parseDouble(uInfo.substring(19)) + toAcc;

    updateInfo(gcString.substring(0,2), uname, df2.format(inAcc));
  }

  public void loseCredit(String lcString) {

  }

  // Returns user info as a string
  public String userInfo(String uname) {
    transactionReader tRead =
      new transactionReader("current_user_accounts_file.txt");

    // Puts current users into an array list for comparing
    ArrayList<String> usrInfo = tRead.readFile();

    String notFound = uname + " not found.";
    String infoStr = "";
    // Get usernames of 15 characters from the user array list and compare
    // with username passed in this function and return true if found.
    for (int i = 0; i < usrInfo.size(); i++) {
      infoStr = usrInfo.get(i).substring(0, 15);
      if (uname.equals(infoStr)) {
        return usrInfo.get(i);
      }
    }
    return notFound;
  }

  public void updateInfo(String type, String uName, String toReplace) {
    try {
      BufferedReader file =
        new BufferedReader(new FileReader("current_user_accounts_file.txt"));
      String line;
      String input = "";
      String check = "";
      ArrayList<String> oldContents = new ArrayList<String>();
      ArrayList<String> newContents = new ArrayList<String>();
      while ((line = file.readLine()) != null) {
        input = line + "\n";
        oldContents.add(line);
      }

      file.close();
      for (int i = 0; i < oldContents.size(); i++) {
        if (oldContents.get(i).substring(0,15).equals(uName)) {
          if (type.equals("02")) {
            newContents.add(oldContents.get(i).replace(oldContents.get(i),
              oldContents.get(i).substring(0,16) + toReplace +
              oldContents.get(i).substring(18) + "\n"));
          }
          else if (type.equals("06")) {
            newContents.add(oldContents.get(i).replace(oldContents.get(i),
              oldContents.get(i).substring(0,19) + toReplace + "\n"));
          }
        } else {
          newContents.add(oldContents.get(i) + "\n");
        }
      }

      FileOutputStream fOut =
        new FileOutputStream("current_user_accounts_file.txt");
      for (int i = 0; i < newContents.size(); i++) {
        fOut.write(newContents.get(i).getBytes());
      }

      fOut.close();

    } catch (Exception e) {
      System.err.println("Error: Cannot read file.");
    }
  }
}

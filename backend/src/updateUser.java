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

  public void deleteUser(String duString) {
    String uname = duString.substring(3, 18);
    String ui = userInfo(uname);
    if(!checkUser(uname)){
      System.out.println("User does not exist");
      return;
    } else if (ui.substring(16,18).equals("  ")) {
      System.out.println("User has already been deleted");
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
    int notZero = 0;
    for (int i = 0; i < credit.length(); i++) {
      if (credit.charAt(i) == '0') {
        notZero++;
      } else if (credit.charAt(i) != 0) {
        break;
      }
    }

    String toConv = credit.substring(notZero);
    Double toAcc = Double.parseDouble(toConv);

    //Now get users current credit
    String uInfo = userInfo(uname);
    Double newBal = Double.parseDouble(uInfo.substring(19)) + toAcc;

    if (newBal > 999999) {
      System.out.println("Error: Cannot exceed maximum credit limit (999999.00)");
    } else {
      updateInfo(gcString.substring(0,2), uname, df2.format(newBal));
    }
  }

  public void loseCredit(String uName, String toLose) {
    String uInfo = userInfo(uName);
    int notZero = 0;
    for (int i = 0; i < toLose.length(); i++) {
      if (toLose.charAt(i) == '0') {
        notZero++;
      } else if (toLose.charAt(i) != 0) {
        break;
      }
    }

    Double giveCred = Double.parseDouble(toLose.substring(notZero));
    Double newBal = Double.parseDouble(uInfo.substring(19)) - giveCred;

    if (newBal < 0) {
      System.out.println("Error: Insufficient funds");
    } else {
      updateInfo("05", uName, df2.format(newBal));
    }


  }

  public void refund(String refString) {
    String toUser = refString.substring(3,18);
    String frmUser = refString.substring(19, 34);
    String refAmt = refString.substring(35);
    String usrType = userInfo(toUser).substring(16,19);

    // Reformat refund string into a addCredit transaction string
    String gcString = "06" + refString.substring(2, 19) + usrType + refAmt;

    gainCredit(gcString);
    loseCredit(frmUser, refAmt);

  }

  // Returns user info as a string
  public String userInfo(String uname) {
    transactionReader tRead =
      new transactionReader(getAccFile());

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
        new BufferedReader(new FileReader(getAccFile()));
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

      // Checks the type for what update to performs
      // 02 = Delete. Takes off user type.
      // 05 = Lose Credit. Take out credit from user info.
      // 06 = Gain credit. Adds credit to user info.
      for (int i = 0; i < oldContents.size(); i++) {
        if (oldContents.get(i).substring(0,15).equals(uName)) {
          if (type.equals("02")) {
            newContents.add(oldContents.get(i).replace(oldContents.get(i),
              oldContents.get(i).substring(0,16) + toReplace +
              oldContents.get(i).substring(18) + "\n"));
          } else if (type.equals("05") || type.equals("06")) {
            newContents.add(oldContents.get(i).replace(oldContents.get(i),
              oldContents.get(i).substring(0,19) + toReplace + "\n"));
          }
        } else {
          newContents.add(oldContents.get(i) + "\n");
        }
      }

      FileOutputStream fOut =
        new FileOutputStream(getAccFile());
      for (int i = 0; i < newContents.size(); i++) {
        fOut.write(newContents.get(i).getBytes());
      }

      fOut.close();

    } catch (Exception e) {
      System.err.println("Error: Cannot read file.");
    }
  }
}

import java.io.*;
import java.util.*;

class updateUser {
  private static String accFile;

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
  public void deleteUser(String uname) {
    if(!checkUser(uname)){
      return;
    }
    try{
      BufferedReader file = new BufferedReader(new FileReader("current_user_accounts.txt"));
      String line;
      String input = "";
      while ((line = file.readLine()) != null){
        input += line + '\n';
        //System.out.println(line);
      }
      file.close();

      input = input.replace(uname + "Delete", uname);
      System.out.println(input);
    }catch(Exception e) {
        System.out.println(e.getMessage());
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

  public void gainCredit(String uname, float credit) {
    // TO DO
  }

  public void loseCredit(String uname, float credit) {
    // TO DO
  }
}

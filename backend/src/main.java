import java.util.*;

class main {
  public static void main(String[] args) {

    //Setting up the transaction reader class to call functions
    transactionReader tr = new transactionReader("transaction_file.txt");

    // Sets up the transaction array list
    ArrayList<String> tStrings = tr.readFile();

    // This is the user accounts file and the events file
    String uFile = "current_user_accounts_file.txt";
    String eFile = "availabletickets.txt";

    // Runs the update
    tr.runUpdate(tStrings, uFile, eFile);


  }
}

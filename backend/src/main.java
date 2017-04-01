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

    /***** THIS IS FOR TESTING PURPOSES ******/
    //updateUser upUsr = new updateUser(uFile);
    //updateTickets upT = new updateTickets(eFile);
    //upT.buyTicket("transaction_file.txt", uFile, "04 Movie Ticket              sellstandard    007 001.00");
    //upT.buyerUpdate("transaction_file.txt", uFile, "sellstandard", "007", "001.00");
    //upUsr.replaceLine("deleteUser     ", "000000.00");
    //upUsr.deleteUser("02 deleteUser      FS 000010.00");
    //upUsr.refund("05 deleteUser      admin           000010.00");
    //System.out.println(upUsr.userInfo("brian          "));

    // Runs the update
    tr.runUpdate(tStrings, uFile, eFile);

  }
}

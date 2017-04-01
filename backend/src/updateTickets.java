import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

class updateTickets {
  private static String evntFile;
  ArrayList<String> evntInfo;
  DecimalFormat df = new DecimalFormat("000000.00");
  DecimalFormat tix = new DecimalFormat("000");

  // Constructor
  public updateTickets(String eFile) {
    evntFile = eFile;
  }

  // Empty constructor
  public updateTickets() {}

  // Getter function for evntFile
  public static String getEvntFile() {
    return evntFile;
  }

  // Setter function for evntFile
  public static void setEvntFile(String evntFile) {
    updateTickets.evntFile = evntFile;
  }

  // Checks if event exists
  public boolean checkEvent(String event) {
    transactionReader tr = new transactionReader("availabletickets.txt");

    // Puts event and ticket info in array list for comparing
    evntInfo = tr.readFile();

    // Gets event name and seller name of the first 41 characters from array
    // list and compares with event name and seller name passed in this function
    // and return true if found.
    for (int i = 0; i < evntInfo.size(); i++) {
      if (event.equals(evntInfo.get(i).substring(0, 41))) {
        return true;
      }
    }
    return false;
  }

  public boolean checkUserEvent(String user){
    transactionReader tr = new transactionReader("availabletickets.txt");

    evntInfo = tr.readFile();

    for (int i = 0; i < evntInfo.size(); i++) {
      if (evntInfo.get(i).contains(user)) {
        return true;
      }
    }
    return false;
  }

  public void newEvent(String neString) {
    // Get name of event and seller of 41 characters from transaction string,
    // which are charcters 3 to 44.
    String newEvnt = neString.substring(3, 44);

    // Performs check to make sure seller is ot making an event they already
    // made then writes to file.
    if(checkEvent(newEvnt)) {
      System.out.println("ERROR: Event already exist!");
      return;
    } else {
      try{
        Writer output;
        output = new BufferedWriter(
          new FileWriter(getEvntFile(), true));

        // neString.substring(3) cuts out the first 3 characters in the
        // transaction string and appends to the file.
        output.append(neString.substring(3) + '\n');
        output.close();
      }catch(Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  public void buyTicket(String accntFile, String neString) {
    String newEvnt = neString.substring(3, 44);
    String seller = neString.substring(29, 44);
    String uName = neString.substring(29,44);
    String cost = neString.substring(49);

    ArrayList<String> newTickets = new ArrayList<String>();
    ArrayList<String> oldTickets = new ArrayList<String>();
    if(!checkEvent(newEvnt)) {
      System.out.println("ERROR: Event does not exist!");
    } else {
      try{
        BufferedReader file = new BufferedReader(new FileReader(getEvntFile()));
        String line;
        String input = "";

        // Reads lines in availabletickets.txt and copies lines into an
        // ArrayList
        while((line = file.readLine()) != null) {
          input = line;
          oldTickets.add(input);
        }

        // Gets quantity of tickets
        int quan = Integer.parseInt(neString.substring(45,48));
        int availableQuan = 0;
        String availableQuanS = "";

        for (int i = 0; i < oldTickets.size(); i++) {
          // If eventname equals the event being bought, get the quantity
          if (newEvnt.equals(oldTickets.get(i).substring(0, 41))) {
            availableQuanS = oldTickets.get(i).substring(43, 45);
            availableQuan = Integer.parseInt(availableQuanS);
            // If remaining tickets are below 0, do not apply change and copy line
            // into newTickets ArrayList.
            // If remaining ticket are 0 or above, apply change and copy into
            // newTickets ArrayList.
            if ((availableQuan - quan) < 0) {
              System.out.println("Error: Not enough tickets left");
              newTickets.add(oldTickets.get(i) + "\n");
            } else {
              int newAvailability = availableQuan - quan;
              // Replaces ticket line info with new available tickets
              newTickets.add(evntInfo.get(i).replace(tix.format(availableQuan), tix.format(newAvailability)) + "\n");
              // Applies credit update to seller
              sellerUpdate(accntFile, uName, neString.substring(45, 48), cost);
            }
          } else {
            newTickets.add(oldTickets.get(i) + "\n");
          }
        }

        // Write newTickets ArrayList into the file.
        FileOutputStream output =
          new FileOutputStream(getEvntFile());
        for (int i = 0; i < newTickets.size(); i++) {
          output.write(newTickets.get(i).getBytes());
        }

        output.close();

      } catch(Exception e) {
        System.out.println(e.getMessage());
      }


    }
  }

  public void deleteEvent(String neString) {
    // TO DO
    String deleteUser = neString.substring(3, 17);

    if(!checkUserEvent(deleteUser)){
      System.out.println("User does not have any events!");
      return;
    }

    try{
      Writer output;
      output = new BufferedWriter(
        new FileWriter(getEvntFile(), false));

      ArrayList<String> newInfo = new ArrayList<String>();
      for (int k = 0; k < evntInfo.size(); k++) {
        if (evntInfo.get(k).contains(deleteUser)) {
          //DO not add
        }else{
          newInfo.add(evntInfo.get(k));
        }
      }

      for (int j = 0; j < newInfo.size(); j++) {
        output.write(newInfo.get(j) + "\n");
      }

      output.close();
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
  }

  public void sellerUpdate(String accntFile, String seller, String amt, String cost) {
    try {
      BufferedReader file =
        new BufferedReader(new FileReader(accntFile));
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

      Double costDouble = Double.parseDouble(cost);

      // Get sellers credit
      String uCred = userCredit(accntFile, seller).substring(19);

      Double ticAmt = Double.parseDouble(amt);

      Double newCredit = Double.parseDouble(uCred) + (costDouble * ticAmt);

      if (newCredit > 999999) {
        System.out.println("Error: User cannot exceed maximum credit limit");
      } else {
        for (int i = 0; i < oldContents.size(); i++) {
          if (oldContents.get(i).substring(0,15).equals(seller)) {
            newContents.add(oldContents.get(i).replace(oldContents.get(i),
            oldContents.get(i).substring(0,19) +
            df.format(newCredit) + "\n"));
          } else {
            newContents.add(oldContents.get(i) + "\n");
          }
          System.out.println(oldContents.get(i) + " | " + newContents.get(i));
        }


        FileOutputStream fOut =
        new FileOutputStream(accntFile);
        for (int i = 0; i < newContents.size(); i++) {
          fOut.write(newContents.get(i).getBytes());
        }

        fOut.close();
      }

    } catch (Exception e) {
      System.err.println("Error: Cannot read file.");
    }
  }

  public String userCredit(String accFile, String user) {
    String input = "";
    try {
     BufferedReader file = new BufferedReader(new FileReader(accFile));
     String line;
     while((line = file.readLine()) != null) {
       if (line.contains(user)) {
         input = line;
       }
     }

     return input;

   } catch (Exception e) {
     System.err.println("Error: File not found");
   }
   return input;
  }

  public void buyerUpdate() {}

}

import java.util.*;
import java.io.*;

class updateTickets {
  private static String evntFile;
  ArrayList<String> evntInfo;

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

  public void buyTicket(String neString) {
    String newEvnt = neString.substring(3, 44);

    if(!checkEvent(newEvnt)) {
      System.out.println("ERROR: Event does not exist!");
      return;
    } else {
      try{
        Writer output;
        output = new BufferedWriter(
          new FileWriter(getEvntFile(), false));

          int quan = Integer.parseInt(neString.substring(45,48));
          int availableQuan = 0;
          String availableQuanS = "";
          for (int i = 0; i < evntInfo.size(); i++) {
            if (newEvnt.equals(evntInfo.get(i).substring(0, 41))) {
              availableQuanS = evntInfo.get(i).substring(43, 45);
              availableQuan = Integer.parseInt(availableQuanS);
              break;
            }
          }

          if((availableQuan - quan) < 0 ){
            System.out.println("ERROR: Not enough tickets left!");
            return;
          }
          String newAvailability = "" + (availableQuan - quan);

          ArrayList<String> newInfo = new ArrayList<String>();
          for (int k = 0; k < evntInfo.size(); k++) {
            if (evntInfo.get(k).contains(newEvnt)) {
              newInfo.add(evntInfo.get(k).replace(availableQuanS, newAvailability));
            }else{
              newInfo.add(evntInfo.get(k));
            }
          }


          for (int j = 0; j < newInfo.size(); j++) {
            output.write(newInfo.get(j) + "\n");
          }

        output.close();
      }catch(Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void sellTicket(String sellString) {
    // TO DO
  }

  public void deleteEvent(String delString) {
    // TO DO
  }

}

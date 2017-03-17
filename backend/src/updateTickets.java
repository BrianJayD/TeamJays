import java.util.*;
import java.io.*;

class updateTickets {

  public boolean checkEvent(String event){
    boolean check = false;
    try {
      File x = new File("available_ticket.txt");
      Scanner sc = new Scanner(x);
      while(sc.hasNext()) {
        if(sc.next().equals(event)){
          System.out.println("TRUE!");
          check = true;
          break;
        }
        //System.out.println(sc.next());
      }
    }
    catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
    return check;

  }

  public void newEvent(String event, String uname, int num, float price){
    if(checkEvent(event)){
      System.out.println("ERROR: Event already exist!");
      return;
    }

    try{
      Writer output;
      output = new BufferedWriter(new FileWriter("available_ticket.txt", true));
      int eventLength = event.length();
      int nameLength = uname.length();
      int priceLength = String.valueOf(price).length();
      String zero= "";
      String space = "";
      String spaceE = "";
      for(int h = 0; h < (25 - eventLength); h++){
        spaceE += " ";
      }
      for(int i = 0; i < (15 - nameLength); i++){
        space += " ";
      }

      for(int j = 0; j < (9 - priceLength); j++){
        zero += "0";
      }

      output.append(event + spaceE +" "+ uname + space + " " + num + " " + zero + price + '\n');
      output.close();
    }catch(Exception e) {
        System.out.println(e.getMessage());
    }

  }

  public void buyTicket(){

  }

  public void sellTicket(){

  }

  public void deleteEvent(){

  }

  /*public static void main(String[] args) {
    updateTickets upTicket = new updateTickets();
    upTicket.newEvent("NewYearBash2", "SaifNIaz", 90, 10.00f);
  }*/
}

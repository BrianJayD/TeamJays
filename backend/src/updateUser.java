import java.util.*;
import java.io.*;

class updateUser {

  public boolean checkUser(String uname){
    boolean check = false;
    try {
      File x = new File("current_user_accounts.txt");
      Scanner sc = new Scanner(x);
      while(sc.hasNext()) {
        if(sc.next().equals(uname)){
          System.out.println("TRUE!");
          check = true;
          break;
        }
      }
    }
    catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
    return check;
  }

  public void deleteUser(String uname){
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

  public void createUser(String uname, String type, float credit){
    if(checkUser(uname)){
      System.out.println("ERROR: username exist");
      return;
    };
    try{
      Writer output;
      output = new BufferedWriter(new FileWriter("current_user_accounts.txt", true));
      int nameLength = uname.length();
      int creditLength = String.valueOf(credit).length();
      String zero= "";
      String space = "";
      for(int i = 0; i < (15 - nameLength); i++){
        space += " ";
      }

      for(int j = 0; j < (9 - creditLength); j++){
        zero += "0";
      }


      output.append(uname + space + type + " " + zero + credit);
      output.close();
    }catch(Exception e) {
        System.out.println(e.getMessage());
    }

  }

  public void gainCredit(String uname, float credit){
    if(!checkUser(uname)){
      System.out.println("ERROR: username does not exist");
      return;
    }

  }

  public void loseCredit(){

  }

  public static void main(String[] args) {
    updateUser upUser = new updateUser();
    upUser.createUser("SaifNIaz", "AA", 000100.00f);
  }

}

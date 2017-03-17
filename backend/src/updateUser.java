import java.io.*;
import java.util.*;

class updateUser {
    private static String fileName;

    public updateUser(String file) {
      fileName = file;
    }

    public String getFileName() {
      return fileName;
    }

    public void createUser(String cUser) {
      transactionReader userReader = new transactionReader("currentaccounts.txt");

      char[] newUsr = cUser.toCharArray();
      char[] usrInfo = new char[28];

      for (int i = 3; i < 31; i++) {
        usrInfo[i-3] = newUsr[i];
      }

      String added = new String(usrInfo);

      System.out.println(added);


    }
}

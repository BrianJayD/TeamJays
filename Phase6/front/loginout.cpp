#include <iostream>
#include <stdio.h>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <vector>
#include "users.h"
#include "loginout.h"

 ofstream fs;

/* process login transaction */
User *login(User *currUser) {

    string username;

    if (currUser->isLoggedIn) {
      cout << "Error: Already logged in.\n";
      return currUser;
    }

    cout << "Enter username:\n>> ";
    getline(cin, username);
    if(userExists(username)) {
      currUser = getUser(username);
      cout << "Login successful.\n";
      currUser->isLoggedIn = true;
      return currUser;
    } else { 
      cout << "Error: Username does not exist.\n";
      return currUser; 
    
    }
}

/* process logout transaction */
User *logout(User *currUser) {
    ofstream transFile;
    /* If user is logged in */
    if(currUser->isLoggedIn) {
        cout << "Logged out.\n";
        printTransactionFile(currUser);
        currUser->isLoggedIn = false;
        totalAddedCredit = 0; //reset total added credit for next session
        transactions.clear(); // reset transactions for next session
        return currUser;
    /* The user is not logged in */
    } else {
        cout << "Error: Must be logged in before logging out.\n";
        return currUser;
    }
}

/* convert input to lowercase */
string toLower(string input) {
	for(int i=0; i < input.length(); i++) {
		input[i] = tolower(input[i]);
	}
	return input;
}

/* check if user exists */
bool userExists(string username) {
    for(int i=0; i < users.size(); i++) {
      if(toLower(username) == toLower(users[i]->getUsername())) { 
        return true; 
      }
    }
    return false;
}

/* writes the formatted transactions out to the transaction file */
void printTransactionFile(User *currUser) {
  fs.open("transaction_file.txt", std::ios_base::app);
  for(int i=0; i < transactions.size(); i++) {
    fs << transactions[i] << endl;
  }
    fs << fmt1("00",currUser->getUsername(),currUser->type,currUser->getBalance()) << endl;
    fs.close();
}

/* format buy, addcredit, dlt, create, and end transactions */
string fmt1(string tranType, string username, string type, float credit) {

  string tran;
	tran.append(tranType);
	tran.append(" ");
	tran.append(username);

    for (int i = username.length(); i <= 15; i++) {
      tran.append(" ");
    }
    tran.append(type);
	  tran.append(" ");
    stringstream ticketString;

    if(tranType == "02") {
       ticketString << setw(9) << setfill('0') << credit;
    } else {
      ticketString << setw(9) << setfill('0') << fixed << setprecision(2) << credit;
    }
    
    string tString = ticketString.str();

    tran.append(tString);
    return tran;
}

/* format sell transaction */
string fmt2(string tranType, string title, string sellername, int tic_sale, float tic_price) {

  string tran, sellString;
  int i;
  tran.append(tranType);
  tran.append(" ");
  tran.append(title);

  for (i = title.length(); i <=25; i++) {
    tran.append(" ");
  }
  tran.append(sellername);
  for (i = sellername.length(); i <=15; i++) {
    tran.append(" ");
  }
  stringstream sellerString;
  sellerString << setw(3) << setfill('0') << tic_sale;
  sellerString << " ";

  sellerString << setw(6) << setfill('0') << fixed << setprecision(2) << tic_price;
  sellString = sellerString.str();

  tran.append(sellString);
  return tran;
}

/* format refund transaction */
string fmt3(string tranType, string toUser, string fromUser, float amt) {

  string tran, cString;
  tran.append(tranType);
  tran.append(" ");
  tran.append(toUser);

  for (int i = toUser.length(); i <= 15; i++) {
    tran.append(" ");
  }
  tran.append(fromUser);

  for (int i = fromUser.length(); i <= 15; i++) {
    tran.append(" ");
  }
  stringstream amtString;

  amtString << setw(9) << setfill('0') << fixed << setprecision(2) << amt;
  cString = amtString.str();
	
  tran.append(cString);

  return tran;

}

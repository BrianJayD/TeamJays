#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <stdio.h>
#include <vector>
#include "users.h"
#include "events.h"
#include "loginout.h"


using namespace std;

//Users.cpp contains all the functions required for implementing a users

/* global variables */
vector<User*> users; //Vector declaration, users holds the users
vector<string> transactions; //a vector that holds current transactions for session
float totalAddedCredit;//holds the total credit amount added per session

//default constructor
User::User() {
  this->type = "";
	this->username = "";
	this->credit = 0.00;
	this->isDeleted = false;
	this->isLoggedIn = false;

}

User::User(string _type, string _username, float _credit) {
	this->type = _type;
	this->username = _username;
	this->credit = _credit;
	this->isDeleted = false;
	this->isLoggedIn = false;
}

/* define constructors for all user classes */
AA_User::AA_User(string type,string username,float credit) : User::User(type,username,credit){};
BS_User::BS_User(string type,string username,float credit) : User::User(type,username,credit){};
SS_User::SS_User(string type,string username,float credit) : User::User(type,username,credit){};

/* Users getter and setter functions */
void User::setUsername(string _username) {this->username = _username;}
void User::setBalance(float _credit) { this->credit = _credit;}
string User::getUsername() { return this->username; }
float User::getBalance() { return this->credit; }

/* process standard user addCredit transaction */
void User::addCredit() {
	string credit;
	float currBalance = this->getBalance();

	cout << "Enter the amount of credit to add:\n>> ";
	getline(cin, credit);

	if( !isValidCredit("06",credit) ) { return; }
	else if ((currBalance += stof(credit)) > 999999.00) {
		cout << "Error: Adding this amount will put you over the max credit.\n";
	}
	else if( (totalAddedCredit + stof(credit)) > 1000 ) {
		cout << "Error: Total added credit for this session exceeds 1000.\n";
	} else {
		totalAddedCredit += stof(credit);
		printf("%.2f credit successfully added.\n", stof(credit));
		transactions.push_back(fmt1("06",this->getUsername(),"  ",stof(credit)));
	}
}

/* process buy transaction for standard user */
void User::buy() {
	string title,username,tickets,strCost,question = "";
	float cost;
	int numTickets;
	stringstream ss;
	User *seller;

	cout << "Enter the seller's name:\n>> ";
	getline(cin, username);

	if( !userExists(username) ) { cout << "Error: User does not exist.\n"; return;}
	else if( toLower(this->getUsername()) == toLower(username) ) {
		cout << "Error: Cannot buy from your own event.\n";
		return;
	}
	else if( getUser(username)->type == "BS" || getUser(username)->isDeleted ) {
		cout << "Error: This user is not selling tickets.\n";
		return;
	}
	seller = getUser(username);
	cout << "Enter the event title:\n>> ";
	getline(cin, title);

	if( !isValidEvent("04",title,username) ) { return; }

	printf("Enter the number of tickets ($%.2f/ticket) to buy:\n>> ", currEvent.ticket_price);
	getline(cin, tickets);

	if( !isValidTicket("04",tickets,this) ) { return; }

	numTickets = stoi(tickets);
	//does not round the cost of the ticket
	ss << setprecision(4) << currEvent.ticket_price * numTickets;
	ss >> cost;
	strCost = to_string(cost);
	strCost = strCost.erase( strCost.find_first_of('.') + 3, std::string::npos );

	if( (currEvent.ticket_count - numTickets) < 0 ) {
		cout << "Error: Not enough tickets available.\n";
		return;
	}
	else if( (this->credit - cost) < 0 ) {
		cout << "Error: You do not have enough credit.\n";
	} else {
		question = "Continue and buy " + tickets + " ticket(s) for $" + strCost + "?\n>> ";
		if( confirmTransac(question) ) {
			cout << "Purchase successful.\n";
			//writes out sell transaction with number of tickets sold
			transactions.push_back(fmt2("03",title,seller->getUsername(),numTickets,currEvent.ticket_price));
			//writes out corresponding buy transaction with number of tickets bought
			//transactions.push_back(fmt2("04", title, this->getUsername(),numTickets,currEvent.ticket_price));
		}
	}
} //end of buy

/* process sell transaction for standard user */
void User::sell() {
	string eventName,num_tickets,ticket_price,question = "";
	username = this->getUsername();

	if ( this->isDeleted ) {
		cout << "Error: You may not perform this action at this time.\n";
		return;
	}
	cout << "Enter in the event name:\n>> ";
	getline(cin, eventName);

	if( !isValidEvent("03",eventName,this->getUsername()) ) { return; }

	cout << "Enter in the number of tickets for sale:\n>> ";
	getline(cin, num_tickets);

	if( !isValidTicket("03",num_tickets,this) ) { return; }

	cout << "Enter in the price for each ticket:\n>> ";
	getline(cin,ticket_price);

	if ( !isValidCredit("03",ticket_price) ) { return; }

	question = "Create event \"" + eventName + "\" with " + num_tickets +
			" ticket(s) ($" + ticket_price + "/ticket)?\n>> ";

	if( confirmTransac(question) ) {
		cout << "Event successfully created.\n";
		transactions.push_back(fmt2("03",eventName,username,stoi(num_tickets),stof(ticket_price)));
	}
} //end of sell

/* process refund,create and delete transaction for unprivileged users */
void User::create() { cout << "You do not have permission to create users.\n"; }
void User::dlt() { cout << "You do not have permission to delete users.\n"; }
void User::refund() { cout << "You do not have permission to refund users.\n"; }

/* process sell transaction for BS user */
void BS_User::sell() { cout << "You do not have permission to sell tickets.\n"; }

/* process buy transaction for SS user */
void SS_User::buy() { cout << "You do not have permission to buy tickets.\n"; }

/* Admin Fuctions */
void AA_User::create() {
	string username, accType, credit, line;
	cout << "Enter in the name of the new user:\n>> ";

	getline(cin, username);
  	int userLength = username.length();

	if (userExists(username)) {
		cout << "Error: User already exists.\n";
		return;
	}
 	else if ((userLength > 15) || (userLength == 0)) {
    	cout << "Error: Username must be between 1 - 15 characters.\n";
		return;
  	}
	cout << "Enter the user's account type:\n>> ";
	getline(cin, accType);

	if( !isValidAccount(accType) ) { return;}
	cout << "Enter in their starting balance:\n>> ";
	getline(cin, credit);

	if( !isValidCredit("01",credit) ) { return; }
	else {
		//print to the transaction file who has been created
		cout << "User successfully created.\n";
		transactions.push_back( fmt1("01", username, accType, stof(credit)) );
	}
}

//Function for deleting people
void AA_User::dlt() {
  string username;
  User *deleteUser;

  cout << "Enter in user to delete:\n>> ";
  getline(cin, username);

  if (username == this->getUsername()) {
    cout << "Error: Cannot delete yourself.\n";
  }
  else if (!userExists(username)) { cout << "Error: User does not exist.\n"; }
  else {
    cout << "User sucessfully deleted.\n";
    deleteUser = getUser(username);
	deleteUser->isDeleted = true;
    //prints the name of the user who was deleted to the transaction file
    transactions.push_back(fmt1("02", username,"  ",0));
  }
}

//Function made for handling refunds between users
void AA_User::refund() {
	string userRefunding, userRefunded, refundAmount;
	User *receiveUser, *depositUser;

	cout << "Enter in the user to refund:\n>> ";
	getline(cin, userRefunded);

	if (!userExists(userRefunded)) {
		cout << "Error: User does not exist.\n";
		return;
	}

	cout << "Enter in the user who is refunding:\n>> ";
	getline(cin, userRefunding);

	if (userRefunding == userRefunded) {
		cout << "Error: A user cannot refund themself.\n";
		return;
	}
	else if (!userExists(userRefunding)) {
		cout << "Error: User does not exist.\n";
		return;
	}

	receiveUser = getUser(userRefunded);
	depositUser = getUser(userRefunding);
	cout << "Enter in the refund amount:\n>> ";
	getline(cin, refundAmount);

	if( !isValidCredit("05",refundAmount) ) { return; }
	else if (stof(refundAmount) > depositUser->getBalance()) {
		cout << "Error: Refunding user does not have enough funds.\n";
	}
	else if (receiveUser->getBalance() + stof(refundAmount) > 999999.00) {
		cout << "Error: Refunded user's account will be put over the max credit.\n";
	} else {
		printf("%.2f credit successfully refunded.\n", stof(refundAmount));
        transactions.push_back(fmt3("05", userRefunded, userRefunding, stof(refundAmount)));
	}
}

//Users add credit which lets them decide who gets credit added to their account
void AA_User::addCredit() {
	string username, credit;
	float currBalance;
	User *user;

	cout << "Enter the user you wish to give credit:\n>> ";
	getline(cin, username);

	if(!userExists(username)) { cout << "Error: User does not exist.\n"; }
	else {
		currBalance = getUser(username)->getBalance();
		cout << "Enter the amount of credit you wish to add:\n>> ";
		getline(cin, credit);

		if( !isValidCredit("06",credit) ) { return; }
		else if( (currBalance + stof(credit)) > 999999.00) {
			cout << "Error: This will put the user over the max credit.\n";
		}
		else if( (totalAddedCredit + stof(credit)) > 1000 ) {
			cout << "Error: Total added credit for this session exceeds 1000.\n";
		}
		else {
			printf("%.2f credit successfully added.\n", stof(credit));
			totalAddedCredit += stof(credit);
			user = getUser(username);
			transactions.push_back(fmt1("06", user->getUsername(),"  ", stof(credit)));
		}
	}
}

/* read in current accounts file */
void loadUsers() {
	string username, type, str;
	float credit;
	int pos;
	ifstream infile;
	infile.open("currentaccounts.txt");

	while( getline(infile, str) )
	{
		// set username
		username = str.substr(0,15);
		pos = username.find_last_not_of(' ');
		username = str.substr(0,pos+1);
		// set account type
		type = str.substr(16,2);
		// set credit
		credit = stof(str.substr(19,9));

		if(str.empty()) { break;}

		User *newUser;
		if(type == "AA")
		{
			newUser = new AA_User(type,username,credit);
		}
		else if(type == "FS")
		{
			newUser = new User(type,username,credit);
		}
		else if(type == "BS")
		{
			newUser = new BS_User(type,username,credit);
		} else
		{
			newUser = new SS_User(type,username,credit);
		}
		users.push_back(newUser);	// store users;

	}
	infile.close();
}

/* get user object */
User *getUser(string username) {
	User *user = new User();
	for(int i=0; i<users.size(); i++) {
		if(toLower(username) == toLower(users[i]->getUsername()))
		{
			return users[i];
		}
	}
	return user;
}

/* checks to see if credit inputs are valid */
bool isValidCredit(string transac,string _credit) {
	float temp,frac,credit;

	if(_credit.find_first_not_of(".0123456789") != std::string::npos) {
		cout << "Error: Invalid input.\n";
		return false;
	}
	credit = stof(_credit);
	temp = credit * 100;
	frac = temp - (int)temp;

	if(frac > 0 || credit < 0.00) {
		cout << "Error: Invalid input.\n";
		return false;
	}
	else if(transac == "03") { //sell
		if(credit > 999.99) {
			cout << "Error: Max ticket price is $999.99.\n";
			return false;
		}
		else if(credit == 0) {
			cout << "Error: Ticket cost must be greater than zero.\n";
			return false;
		}
	}
	else if(transac == "06" && credit == 0) { //addcredit
		if(credit == 0) {
			cout << "Error: Cannot add zero credit.\n";
			return false;
		}
		else if(credit > 999999.00) {
			cout << "Error: Credit exceeds the maximum amount.\n";
			return false;
		}
	}
	else if(transac == "05" && credit == 0) { //refund
		cout << "Error: Cannot refund zero credit.\n";
		return false;
	} else { return true; }
}

/* checks to see if the number of tickets for  sale is correct */
bool isValidTicket(string transac,string tickets, User *currUser) {
	if(tickets.find_first_not_of("0123456789") != std::string::npos) {
		cout << "Error: Invalid input.\n";
		return false;
	}
	int numTickets = stoi(tickets);
	if( numTickets <= 0 ) {
		cout << "Error: Tickets must be greater than zero.\n";
		return false;
	}else {
		if(transac == "03" && numTickets > 100) {
			cout << "Error: The max number of tickets for events is 100.\n";
			return false;
		}
		else if(transac == "04" && currUser->type != "AA" && numTickets > 4) {
			cout << "Error: You may purchase at most 4 tickets per transaction.\n";
			return false;
		}
		return true;
	}
}

/* confirm transaction */
bool confirmTransac(string question) {
	string answer;
	cout << question;
	getline(cin,answer);
	if(toLower(answer) == "yes") { return true; }
	else if(toLower(answer) == "no") {
		cout << "Transaction cancelled.\n";
		return false;
	} else {
		cout << "Error: Invalid answer.\n";
		return false;
	  }
}

/* checks to see if the account type is valid. */
bool isValidAccount(string account) {
	if(account == "AA" || account == "FS" || account == "BS" || account == "SS") {
		return true;
	} else {
		cout << "Error: Invalid account type.\n";
		return false;
	}
}

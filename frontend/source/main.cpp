#include <iostream>
#include <stdio.h>
#include <string>
#include <vector>
#include "users.h"
#include "events.h"
#include "loginout.h"
using namespace std;

/* 
	Basic Ticket Selling program that allows users
	to buy and sell tickets. The supported transactions
	include: addcredit, buy, sell, refund, create, delete, 
	login, logout (ends session), and quit to exit the program.

	Run: "make" to build and "make run" to run the program.
*/

int main(int argc, char const *argv[]) 
{
	User *currUser = new User(); //create new user;
	bool waitingForInput = true;
	string transac;

	loadUsers();
	loadEvents();

	cout << "Welcome to the Ticket Selling Service.\n";

	while(waitingForInput) {
		cout << ">> ";
		getline(cin, transac);
		if (transac == "quit") { exit(0); }
		if( !currUser->isLoggedIn && toLower(transac) == "login" ) {currUser = login(currUser);}
		else if(currUser->isLoggedIn) {
			if(toLower(transac) == "login") { currUser = login(currUser); }
			else if(toLower(transac) == "logout") { currUser = logout(currUser);}
			else if(toLower(transac) == "addcredit") { currUser->addCredit(); }
			else if(toLower(transac) == "buy") { currUser->buy(); }
			else if(toLower(transac) == "sell") { currUser->sell(); }
			else if(toLower(transac) == "create") { currUser->create(); }
			else if(toLower(transac) == "delete") { currUser->dlt(); }
			else if(toLower(transac) == "refund") { currUser->refund(); }
			else {cout << "Error: Unknown transaction command.\n"; }

		} else {cout << "Error: You must first login before making transactions.\n"; }

	}

} //end of Main

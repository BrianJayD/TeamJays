#include <iostream>
#include <stdio.h>
#include <fstream>
#include <string>
#include <vector>
#include "users.h"
#include "events.h"
#include "loginout.h"

using namespace std;

/* global variables */
vector<Event> events;
Event currEvent;

/* given the seller and event name, check if the seller is
   selling tickets for that event
*/
bool eventExists(string seller, string title) {
    for (int i=0; i <events.size(); i++) {
		if( toLower(events[i].title) == toLower(title) &&
		toLower(events[i].seller) == toLower(seller) ) {
			currEvent = events[i];
			return true;
		}
    } return false;
}

/* read in available tickets file */
void loadEvents() {
	string str;
    fstream infile;
	infile.open("availabletickets.txt");

	int pos,j, i = 0;
	while( getline(infile, str) )
	{
		if(str.empty()) {break;}

		events.push_back(Event());

		//set event name
		pos = str.substr(0,25).find_last_not_of(' ');
		events[i].title = str.substr(0,pos+1);

		//set seller name
		pos = str.substr(26,15).find_last_not_of(' ');
		events[i].seller = str.substr(26,pos+1);

		//set ticket count
		events[i].ticket_count = stoi(str.substr(42,3));

		//set ticket ticket_price
		events[i].ticket_price = stof(str.substr(46,6));
		i++;
	}
}

/* checks to see if event is valid for buy or sell transactions */
bool isValidEvent(string transac,string title,string seller) {
	int length = title.length();
	if ( length == 0 || length > 25 ) {
		cout << "Error: Invalid title.\n";
		return false;
	}
	else if(transac == "03") {
		if( eventExists(seller,title) ) {
			cout << "Error: You have an event with this name.\n";
			return false;
		}return true;
	} else {
		if( !eventExists(seller, title) ) {
			cout << "Error: This user is not selling tickets for this event.\n";
			return false;
		}
		return true;
	}
}

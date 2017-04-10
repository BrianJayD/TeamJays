#ifndef EVENTS_H_
#define EVENTS_H_

using namespace std;

extern vector<User*> users;

struct Event {
	float ticket_price;
	int ticket_count;
	string title, seller;
};

/* check to see if the event for a user exists */
bool eventExists(string seller,string title);

/* read in the available tickets file */
void loadEvents();

/* check to see if event is valid for buy/sell transaction */
bool isValidEvent(string transac,string title,string seller);

#endif

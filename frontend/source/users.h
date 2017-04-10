 #ifndef USERS_H_
 #define USERS_H_

using namespace std;
struct Event; //forward declaration of struct
extern Event currEvent;

//full standard class
class User {
	protected:
		float credit;
		string username;
	public:
		bool isDeleted, isLoggedIn;
		string type;
		User(); //default constructor
		User(string type,string username,float credit); //overloaded constructor
		void setUsername(string _username);
		void setBalance(float credit);
		string getUsername();
		float getBalance();
		virtual void addCredit();
		virtual void buy();
		virtual void sell();
		virtual void create();
		virtual void dlt();
		virtual void refund();
		~User();
};

//buy standard class
class BS_User : public User {
	public:
		BS_User();
		BS_User(string type,string username,float credit);
		void sell();
		~BS_User();
};
//sell standard struct
class SS_User : public User {
	public:
		SS_User();
		SS_User(string type,string username,float credit);
		void buy();
		~SS_User();
};

//admin class
class AA_User : public User {
	public:
		AA_User();
		AA_User(string type,string username,float credit);
		void create();
		void dlt();
		void addCredit();
		void refund();
		~AA_User();
};

/* returns user object */
User *getUser(string username);

/* reads in users from current accounts */
void loadUsers();

/* stores successful transaction for the current session */
void saveTransaction(string transLine);

/* checks to see if credit is valid */
bool isValidCredit(string transac, string credit);

/* checks to see if account type is valid */
bool isValidAccount(string account);

/* checks if ticket amount for sell sell/buy transac is valid */
bool isValidTicket(string transac,string tickets, User *currUser);

/* finalize transaction, allowing user to cancel if wanted */
bool confirmTransac(string question);

/* check to see if event is valid for buy/sell transaction */
bool isValidEvent(string transac,string title,string seller);

#endif

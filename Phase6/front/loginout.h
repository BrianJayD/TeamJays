 #ifndef LOGINOUT_H_
 #define LOGINOUT_H_

using namespace std;

/* extern variables */
extern vector<User*> users;
extern vector<string> transactions;
extern float totalAddedCredit;
extern User *currUser;

/* process login transaction */
User *login(User *currUser);

/* process logout transaction */
User *logout(User *currUser);

/* convert input to lowercase for comparison  */
string toLower(string input);

/* check if user exists */
bool userExists(string username);

/* write out transactions to transaction file */
void printTransactionFile(User *currUser);

/*Format buy, addcredit, dlt, create, and end transactions */
string fmt1(string tranType, string username, string type, float credit);

/* Format sell transaction */
string fmt2(string tranType, string title, string sellername, int tic_sale, float tic_price);

/* Format refund transaction */
string fmt3(string tranType, string toUser, string fromUser, float amt);

#endif

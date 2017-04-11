#! /bin/bash
# This script runs 5 days weekly transactions.

# First we run our FrontEnd of the ticket seller.
# For which we had to change to our front end directory
# and use the frontend 'make', which will compile our system.

cd front

make

cd ..

# Next we have to run our BackEnd of the ticket seller.
# We used loop to run a week's transaction. We then used our inputs
# for week and run them on Our FrontEnd, and checked it with our BackEnd.

javac -d ./ back/main.java back/transactionReader.java back/updateTickets.java back/updateUser.java

for (( x = 1; x <= 5; x++ ))
do
  ./front/ticket-seller < input/weekly_test$x.inp

  java main

# We had to clear the transaction file after the system ran,
# to make room for futre transaction.

  > transaction_file.txt #reset transaction file to be empty
done

rm *.class

#! /bin/bash

cd front

make

cd ..

javac -d ./ back/main.java back/transactionReader.java back/updateTickets.java back/updateUser.java

for (( x = 1; x <= 2; x++ ))
do
  ./front/ticket-seller < input/weekly_test$x.inp

  java main

  > transaction_file.txt #reset transaction file to be empty
done

rm *.class

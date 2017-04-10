#! /bin/bash

cd front

make

cd ..

javac -d ./ back/main.java back/transactionReader.java back/updateTickets.java back/updateUser.java

./front/ticket-seller < input/daily_test.inp

java main

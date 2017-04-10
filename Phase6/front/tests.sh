#! /bin/bash
clear
make

tests=(login logout addcredit buy create delete refund sell)
echo -e "\nStarting tests. . ."
for i in ${tests[*]}
do
  echo -e "\n" $i "tests\n"
  result=$(ls ./test_expected/$i/ | wc -l)
  numTests=$(($result / 3))

  for (( x = 1; x <= $numTests; x++ ))
  do
    > transaction_file.txt #reset transaction file to be empty
   
    # save terminal output to actual terminal output files
    ./ticket-seller < ./test_expected/$i/$i$x.inp > ./test_actual/$i/$i$x.out
    # copy the generated transaction files to each actual transaction files
    cp transaction_file.txt ./test_actual/$i/$i$x.atf

    # compares expected terminal output with actual output
    cmp -s ./test_expected/$i/$i$x.out ./test_actual/$i/$i$x.out
    test1=$? #get return code of last cmp command
    # compares expected transaction file (.etf) with actual transaction file (.atf)
    cmp -s ./test_actual/$i/$i$x.atf ./test_expected/$i/$i$x.etf
    test2=$? #get return code of last cmp command

    if [ $test1 == 0 ] && [ $test2 == 0 ]; then
      echo $i $x Terminal: Pass
      echo $i $x Transacs: Pass
    elif [ $test1 -eq 0 ] && [ $test2 -gt 0 ]; then
      echo $i $x Terminal: Pass
      echo $i $x Transacs: Fail
    elif [ $test1 -gt 0 ] && [ $test2 -eq 0 ]; then
      echo $i $x Terminal: Fail
      echo $i $x Transacs: Pass
    else
       echo $i $x Terminal: Fail
       echo $i $x Transacs: Fail
    fi

  done
done

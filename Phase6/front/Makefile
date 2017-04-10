.PHONY: all clean

out: main.cpp users.cpp loginout.cpp
	g++ -std=c++11 *.cpp -o ticket-seller

run: ticket-seller
	./ticket-seller

all: out run

clean:
	rm ticket-seller
	rm transaction_file.txt
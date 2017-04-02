import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

public class test {
	
    String uFile = "current_user_accounts_file.txt";
    String eFile = "availabletickets.txt"; 
    String tranFile = "transaction_file.txt";   
    //Stack s = new Stack();

    @Test
    //Test for Create User
    //Loop coverage zero times 
    //Imagining that account file has nothing on it
    public void mytest() {
        updateUser upU = new updateUser(uFile);
        
        assertEquals(false,upU.checkUser("Said"));
    }

    @Test
    //Loop coverage one times
    public void test1() {
        updateUser upU = new updateUser(uFile);
        //upU.createUser("01 Saif            FS 000009.00");       
        assertEquals(true,upU.checkUser("admin"));
    }

    @Test
    //Loop coverage two times
    public void test2() {
        updateUser upU = new updateUser(uFile);
        //upU.createUser("01 Saif            FS 000009.00");       
        assertEquals(true,upU.checkUser("RobinBanks"));
    }

    @Test
    //Loop coverage many times and Decision Coverage
    public void test3() {
        updateUser upU = new updateUser(uFile);
        upU.createUser("01 Saif            FS 000009.00");       
        assertEquals(true,upU.checkUser("Saif"));
    }

    @Test
    // Decision Coverag
    public void test4() {
        updateUser upU = new updateUser(uFile);
        //upU.createUser("01 Mike            FS 000009.00");       
        assertEquals(false,upU.checkUser("Sam"));
    }

    //Delete User
    @Test
    // Decision Coverag
    public void test5() {
        updateUser upU = new updateUser(uFile);
        upU.deleteUser("02 Saif            FS 000010.00");       
        assertEquals(false,upU.checkUser("Saif            FS"));
    }

    @Test
    // Decision Coverag
    public void test6() {
        updateUser upU = new updateUser(uFile);
        //upU.deleteUser("02 Saif            FS 000010.00");       
        assertEquals(true,upU.checkUser("Saif              "));
    }



    @Test
    //Test for Create Event and CheckEvent------------------------------------------------------------------------
    //Loop coverage zero times
    //Imaging that the ticket available is zero
    public void testT1(){
        updateTickets upT = new updateTickets(eFile);
    	//upT.newEvent("03 Movie Ticket              sellstandard    007 001.00");
    	assertEquals(false,upT.checkEvent("Event Ticket              sellstandard"));
    }

    @Test
    // Loop Coverage one time
    public void testT2() {
        updateTickets upT = new updateTickets(eFile);
        //upT.newEvent("03 Movie Ticket              sellstandard    007 001.00");
        assertEquals(true,upT.checkEvent("preMadeEvent              preSetSeller"));
    }

    @Test
    // Loop Coverage two time
    public void testT3() {
        updateTickets upT = new updateTickets(eFile);
        //upT.newEvent("03 Movie Ticket              sellstandard    007 001.00");
        assertEquals(true,upT.checkEvent("myOwnEvent                admin"));
    }

    @Test
    // Loop Coverage many time and Decision Coverage
    public void testT4() {
        updateTickets upT = new updateTickets(eFile);
        upT.newEvent("03 Movie Ticket              sellstandard    007 001.00");
        assertEquals(true,upT.checkEvent("Movie Ticket              sellstandard"));
    }

    @Test
    // Decision Coverage
    public void testT5() {
        updateTickets upT = new updateTickets(eFile);
        
        assertEquals(false,upT.checkEvent("Event Ticket              sellstandard"));
    }
    
    //@Test
    // Buy Ticket---------------------------------------------------------------------------------------------
    // Decision Coverage
    /*public void testT6() {
        updateTickets upT = new updateTickets(eFile);
        upT.buyTicket(tranFile, uFile, "04 preMadeEvent              preSetSeller    002 001.00");
        assertEquals(true,upT.checkEvent("preMadeEvent              preSetSeller    048"));
    }

    @Test
    public void testT7() {
        updateTickets upT = new updateTickets(eFile);
        upT.buyTicket(tranFile, uFile, "04 preMadeEvent              preSetSeller    003 001.00");
        assertEquals(false,upT.checkEvent("preMadeEvent              preSetSeller    050"));
    }*/

    //Statement Coverage
    @Test
    public void testT8() {
        updateTickets upT = new updateTickets(eFile);
        upT.buyTicket("transaction_file1.txt", uFile, "04 preMadeEvent              preSetSeller    003 001.00");
        assertEquals(true,upT.checkEvent("preMadeEvent              preSetSeller    047"));
    }

    @Test
    public void testT9() {
        updateTickets upT = new updateTickets(eFile);
        upT.buyTicket(tranFile, eFile, "04 preMadeEvent              preSetSeller    003 001.00");
        assertEquals(true,upT.checkEvent("preMadeEvent              preSetSeller    044"));
    }

    @Test
    public void testT10() {
        updateTickets upT = new updateTickets(eFile);
        upT.buyTicket(tranFile, uFile, "04 preMadeShows              preSetSeller    003 001.00");
        assertEquals(false,upT.checkEvent("preMadeEvent              preSetSeller    036"));
    }

    // Delete Ticket and CheckUserEvent------------------------------------------------------------------------------------------
    //Statement Coverage
    @Test
    public void testT11() {
        updateTickets upT = new updateTickets(eFile);
        upT.deleteEvent("02 sellstandard     FS 000010.00");
        assertEquals(false,upT.checkUserEvent("sellstandard"));
    }

    @Test
    public void testT12() {
        updateTickets upT = new updateTickets(eFile);
        upT.deleteEvent("02 sellstandard      FS 000010.00");
        assertEquals(true,upT.checkUserEvent("admin"));
    }



    public static junit.framework.Test suite(){
       return new JUnit4TestAdapter(test.class);
    }
}

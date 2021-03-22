package newbank.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;


public class Register {
    NewBank r = new NewBank();
    //private static final Register register = new Register();

    public PrintWriter out;
    public BufferedReader in;
/*
    public Register(Socket s) throws IOException {
        //bank = NewBank.getBank();
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }*/

    private String getNewStg (){
        String newStg = "";
        int i = 0;
        try {
            do {
                i = System.in.read();
                if (i != '\n')
                    newStg += (char) i;
            } while (i != '\n');
        }
        catch (IOException e)
        {

        }
        return newStg;
    }

    public void run() {
        boolean valid = false;
        String userId;
        HashMap<String, Customer> customers = r.getCustomersMap();
        HashMap<String, String> passwords = r.getPasswordsMap();

      //  private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

        System.out.println("Please enter a username you would like to use");
        String userID = getNewStg();

        //for loop to loop back round if username is take and need to ask again

        if (customers.get(userID) != null) {
            System.out.println("Sorry this username is taken, please try again");
        } else {
            System.out.println("Please enter a valid password");
            String password = getNewStg();

            System.out.println("Please enter a Account type");
            String AccType = getNewStg();

            System.out.println("Please enter a Deposit amount");
            String DepositValue = getNewStg();
            double value = Double.parseDouble(DepositValue);
            Customer newC = new Customer();
            Account newA = new Account(AccType,value);
            newC.addAccount(newA);
            customers.put(userID,newC);
            passwords.put(userID,password);
        //    cutomers.put(userID, Customer);
        }

/*
        do {
            out.println("please create a password which is more than 6 characters long");

            String pass = in.readLine();
            if (pass.length() < 7)
                valid = false;
            out.println("This password is less than 7 characters, it needs to be 7 characters or more in length, please try again");
            continue;

            for (int i = 0; i < pass.length(); i++) {
                char c = pass.charAt(i);

                if (
                        ('a' <= c && c <= 'z')
                                || ('A' <= c && c <= 'Z')
                                || ('0' <= c && c <= '9')
                ) {

                    valid = true;
                } else {

                    System.out.println("Only letter & digits are acceptable.");
                    valid = false;
                    break;
                }

            }
        }

        while (!valid); // verify if the password is valid, if not repeat the process
        passwords.put(userId, pass);

        out.println("Great, you are all set up, would you like to login? answer Yes or No");
        // String next = in.readLine();
        // if(next.equals("yes"){
        //if(next.equals("Yes"){
        // return


*/
    }
}




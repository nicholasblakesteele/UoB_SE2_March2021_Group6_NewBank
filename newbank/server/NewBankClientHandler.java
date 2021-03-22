package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	//private Register register;
	private ServerSocket server;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);

	}

	private void newRegister(){
		try {
			out.println("Please enter a username you would like to use");
			String userID = in.readLine();

			//for loop to loop back round if username is take and need to ask again

			if (bank.validateID(userID) == true) {
				out.println("Sorry this username is taken, please try again");
			} else {

				out.println("Please enter a valid password");
				String password = in.readLine();
				//for (int i = 0; bank.checkPassword(password); i++)//need to loop this functon back to ask for valid password again
				while(bank.checkPassword(password) == true) {
					out.println("Password too short, must be more than 7 characters");
				//	i++;
				}


				out.println("Please enter a Account type");
				String AccType = in.readLine();

				out.println("Please enter a Deposit amount");
				String DepositValue = in.readLine();
				double value = Double.parseDouble(DepositValue);
				Account newA = new Account(AccType, value);
				bank.addNewCustomer(userID, password, newA);
				out.println("Success");
				//    cutomers.put(userID, Customer);
			}
		}
		catch (IOException r)
		{
			System.out.println(r);
		}
	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			// ask for user name
			String newline = System.getProperty("line.separator");
			out.println("Would you like to register or login, type number accordingly" + newline + "1.register" + newline + "2.login");
			String options = in.readLine();
			if (options.equals("1")) {
				newRegister();
			}
			else if(options.equals("2"))


				try {

					out.println("Please enter your username");
					String userName = in.readLine();
					out.println("Enter Password");
					String password = in.readLine();
					out.println("Checking Details...");
					// authenticate user and get customer ID token from bank for use in subsequent requests
					CustomerID customer = bank.checkLogInDetails(userName, password);
					// if the user is authenticated then get requests from the user and process them
					if (customer != null) {
						out.println("Log In Successful");
						while(true) {
							out.println("---------------");
							out.println("What do you want to do?" +
									"\nPress 1 to view your account" +
									"\nPress 2 to deposit/withdraw/transfer/move --NOT YET IMPLEMENTED--");
							String request = in.readLine();
							System.out.println("Request from " + customer.getKey());
							String responce = bank.processRequest(customer, request);
							out.println(responce);
						}
					} else {
						out.println("Log In Failed");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						in.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}

			} catch(IOException e){
				e.printStackTrace();
			}


	}
}

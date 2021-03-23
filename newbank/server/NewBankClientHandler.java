package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;


	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	private void printClientMajorHelpCommands() {

		out.println("\n---------------------------------------------------------");
		out.println("What do you want to do today?");
		out.println("Press 1 to list your accounts");
		out.println("Press 2 to create a new account");
		out.println("Press 3 to transfer money between your accounts");
		out.println("Press 4 to send money to another person");
		out.println("Press 5 to logout");
		out.println("\nEnter option: "); // Note use of print to enter command on same line

	}

	public void run() {

		// keep getting requests from the client and processing them

		try {
	    // ask for user name
	    String newline = System.getProperty("line.separator");
	    out.println("Would you like to register or login, type number accordingly" + newline + "1.register" + newline + "2.login");
	    String options = in.readLine();

			if (options.equals("1")) {

			  newAccountRegister();

			}

			else if(options.equals("2")) {

				existingAccountLogin();
			}

		}

	}

	private void existingAccountLogin() {

		try {

			// ask for user name
			out.println("Enter Username: "); // Note use of print to enter command on same line
			String userName = in.readLine();
			// ask for password
			out.println("Enter Password: "); // Note use of print to enter command on same line
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them

			if (customer != null) {

				out.println("Log In Successful for: " + customer.getKey());

				while(true) {

					printClientMajorHelpCommands();

					String request = in.readLine();
					out.println("\nProcessing request for customer: " + customer.getKey());

					String response = bank.processRequest(customer, request);

					// Print status of request

					switch(request) {

						case "1" :
							// List your accounts
							out.println("Retrieving accounts...");

						case "2" :
							// Create a new account
							// out.println("UI message");

						case "3" :
							// Transfer money between your accounts
							// out.println("UI message");

						case "4" :
							// Send money to another person
							// out.println("UI message");

						case "5" :
							// Logout
							// out.println("UI message");

						default :
							// Print nothing
					}

					out.println(response);

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

	}

	private void newAccountRegister(){

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

}

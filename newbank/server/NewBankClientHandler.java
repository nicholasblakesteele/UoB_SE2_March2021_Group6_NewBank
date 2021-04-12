package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

		out.println();

		out.println(" ╭────────────────────── Main Menu ──────────────────────-─╮");
		out.println(" │ What would you like to do today?                                                                             │");
		out.println(" │ Press 1 to list your accounts                                                                                          │");
		out.println(" │ Press 2 to create a new account                                                                                  │");
		out.println(" │ Press 3 to transfer money between your accounts                                             │");
		out.println(" │ Press 4 to send money to another person                                                              │");
		out.println(" │ Press 5 to apply for a Microloan                                                                                  │");
		out.println(" │ Press 6 to log out                                                                                                               │");
		out.println(" ╰───────────────────────────────────────────────────────╯");
		out.println("\nEnter option: "); // Note use of print to enter command on same line

	}

	public void run() {

		// keep getting requests from the client and processing them

		try {
	    // ask for user name
	    String newline = System.getProperty("line.separator");
	    out.println("Would you like to register or login, type number accordingly" + newline + "1. Register" + newline + "2. Login");
	    String options = in.readLine();

			if (options.equals("1")) {

			  newAccountRegister();

			}

			else if(options.equals("2")) {

				existingAccountLogin();
			}

		} catch(IOException e) {

			e.printStackTrace();
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

				while (true) {

					printClientMajorHelpCommands();

					String request = in.readLine();
					out.println("\nProcessing request for customer: " + customer.getKey());

					//Commenting out the pauseUI function. It interferes with the main menu (throws an error message)
					//pauseUI();
					//String response = bank.processRequest(in, out, customer, request);


					// Print status of request

					switch(request) {

						case "1" :
							// List your accounts
							out.println("Retrieving accounts...\n");

						case "2" :
							// Create a new account
							// out.println("UI message");

						case "3" :
							// Transfer money between your accounts
							// out.println("UI message");

						case "4" :
							// Send money to another person
							// out.println("UI message");

						case "5":
							// Apply for microloan
							// out.println("UI message");

						case "6" :
							// Logout
							// out.println("UI message");

						default :
							// Print nothing
					}

					String response = bank.processRequest(in, out, customer, request);
					out.println(response);

					//Commenting out the pauseUI function. It interferes with the main menu (throws an error message)
					//pauseUI();

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

	// Function to pause the UI after displaying a menu
	private void pauseUI(){
		out.println("\nPress Enter to continue...");
		try
		{
			in.read();
		}
		catch(Exception e)
		{}
	}



	// Function to return the initial deposit value entered by the user at registration
	private double getValue(){

		double value = 0.0;

		Boolean correctValue = false;

		// Asking user for deposit, loops until a correct value is entered
		while (correctValue == false) {

			// User enters an integer value
			try{
				String input = in.readLine();

				value = Double.parseDouble(input);

			}catch(Exception e){
				System.out.println("Not an integer");
			}

			// Denies the value if it is out of limits
			if (value <= 1 || value >= 1000000) {
				out.println("Our apologies, the amount entered was not accepted.");
				out.println("The bank allows deposits between 1 £ and 1'000'000 £, please try again: \n");
			}
			else {
				correctValue = true;
			}
		}
		return value;
	}

	private void newAccountRegister(){

		try {
			out.println("Please enter your desired username");
			String userID = in.readLine();

			//for loop to loop back round if username is take and need to ask again

			if (bank.validateID(userID) == true) {
				out.println("Sorry this username is taken, please try again");

				newAccountRegister(); // Need to loop back again if error

			} else {


				Boolean passwordPass = false;
				String password = null;

				while (passwordPass == false) {

					if (password != null) {

						out.println("This password is not secure");
					}

					out.println("Please enter a secure password");
					out.println("The password should be at least 7 characters and include at least one digit, one uppercase, one lowercase, and one of the following special characters: @#$%^&+=!?");

					password = in.readLine();

					passwordPass = bank.checkPassword(password);
				}


				out.println("Please enter account name");
				String accountName = in.readLine();

				out.println("Please enter initial deposit");

				double value = getValue();


				Account newA = new Account(accountName, value);
				bank.addNewCustomer(userID, password, newA);

				// Note currently new Account() will only create an account in memory

				out.println("Thank you. Customer account and opening balance created. Please login.");

				// Now offer to login

				existingAccountLogin();

			}

		} catch (IOException r) {

			System.out.println(r);
		}
	}

}

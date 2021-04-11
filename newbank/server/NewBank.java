// NBS group branch change

package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private HashMap<String,String> passwords;

	public HashMap<String, Customer> getCustomersMap(){
		return customers;
	}

	public HashMap<String, String> getPasswordsMap() {
		return passwords;
	}

	private NewBank() {
		customers = new HashMap<>();
		passwords = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);

	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized boolean validateID(String userID){
		if(customers.containsKey(userID) ){
			return true;
		}
		return false;
	}

	public synchronized void addNewCustomer(String UserId, String password, Account newA){
		Customer newC = new Customer();
		newC.addAccount(newA);
		customers.put(UserId, newC);
		passwords.put(UserId, password);
	}

	// Function validates password, requires at least 7 characters including: one digit, one uppercase, one lowercase, one special character @#$%^&+=
	public synchronized boolean checkPassword(String password) {
		boolean valid = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{7,}$").matcher(password).matches();
		if (valid)
			return true;
		else {
			return false;
		}
	}


	//Not yet implemented!!
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return new CustomerID(userName);
	}

	// ------------------------------
	// commands from the NewBank customer are processed in this method
	// ------------------------------

	public synchronized String processRequest(BufferedReader in, PrintWriter out, CustomerID customer, String request) {

		if (customers.containsKey(customer.getKey())) {

			switch(request) {

				case "1" :
					// List your accounts
					return showMyAccounts(customer);

				case "2" :
					// Create a new saving or checking account
					createNewAccount(in, out, customer);
					//Always display all account summary after creating a new account
					return showMyAccounts(customer);

				case "3" :
					// Transfer money between your accounts
					transBetweenAccount(in,out,customer);
					return showMyAccounts(customer);

				case "4" :
					// Send money to another person
					return "SendMoney Feature Incomplete";

				case "5" :
					// Microloan request
					requestMicroloan(in, out);
					return "Thank you for using our micro loan service";

				case "6" :
					// Logout
					return "Logout Feature Incomplete";

				default :
					return "Invalid option. Please try again.";
			}
		}

		return "Account does not exist";
	}


	//Create a new account. NOTE: Currently it does not check if an account with the same name exists!
	private void createNewAccount(BufferedReader in, PrintWriter out, CustomerID customer) {
		Customer client = customers.get(customer.getKey());
		out.println("Enter your new account name, Savings or Checking:  ");
		String newAccountName = null;
		try {
			newAccountName = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Ask for a deposit amount
		double value = 0.0;
		while(true) {
			out.println("Please enter initial deposit:  ");
			String DepositValue = null;
			try {
				DepositValue = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//guard against bad inputs!
			try {
				value = Double.parseDouble(DepositValue);
				if(value > 0){
					break;
				}
			} catch (NumberFormatException e) {
				out.println("Please enter only integers greater than 0");
			}
		}
		//add the new account and initial deposit value  to customer
		client.addAccount(new Account(newAccountName, value));
	}

	//Transfer money between accounts.
	//TO DO: Functions can be broken down into smaller functions.
	private void transBetweenAccount(BufferedReader in, PrintWriter out, CustomerID customer){

		Customer client = customers.get(customer.getKey());
		//Retrieve transfer from info
		String transferFrom = "";
		String transferTo = "";
		while(true) {
			out.println("Where do you want to transfer from?:  ");
			try {
				transferFrom = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (transferFrom.equalsIgnoreCase("savings") || transferFrom.equalsIgnoreCase("checking")) {
				break;
			}
			else if(!client.accountExists(transferFrom)){
				out.println("You do not have a " + transferFrom + " account. Please try again.");
			}
			else if(!client.accountExists(transferTo)){
				out.println("You do not have a " + transferTo+ " account. Please try again.");
			}
			else{
				out.println("An error has occurred. Returning to the main menu.");
				return;
			}
		}

		//Determine the transferTo account
		if(transferFrom.equalsIgnoreCase("savings")){
			transferTo = "Checking";}
		else{
				transferTo = "Savings";}

		//Retrieve the customer's current transferFrom balance
		double balanceFrom = client.showBalance(transferFrom);
		double balanceTo = client.showBalance(transferTo);

		//Amount to transfer between accounts
		//Check to see if there are sufficient funds in the transferFrom account
		//TO DO: Should add other options to start over, return to the main menu, etc.
		String value = null;
		double transferAmount = 0.0;
		while(true) {
			out.println("How much would you like to transfer?:  ");
			try {
				value = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Guard against bad inputs!
			try {
				transferAmount = Double.parseDouble(value);
				if (transferAmount > 0 && transferAmount <= balanceFrom) {
					break;
				} else if(balanceFrom == 0){
					out.println("You do not have any money in your " + transferFrom +  " account. Returning to the Main Menu.");
					return;
				} else if (transferAmount > balanceFrom) {
					out.println("Insufficient funding. Please enter the amount again.");
				} else{
					out.println("Please enter an amount greater than 0");
				}
			} catch (NumberFormatException e) {
				out.println("Please enter only integers greater than 0");
			}
		}

		//Transfer (Also TO DO: Create another method to break down the process!
		balanceFrom -=  transferAmount;
		balanceTo +=  transferAmount;
		client.updateBalance(transferFrom, balanceFrom);
		client.updateBalance(transferTo, balanceTo);

	}


	private String showMyAccounts(CustomerID customer) {

		return (customers.get(customer.getKey())).accountsToString();
	}


	private void requestMicroloan(BufferedReader in, PrintWriter out) {

		out.println("Requesting microloan...");

		try {

			out.println("How much would you like to borrow? (Min: £100. Max £10,000)");
			String requestedAmount = in.readLine();
			double requestedAmountValue = Double.parseDouble(requestedAmount);

			if (requestedAmountValue < 100 || requestedAmountValue > 10000) {

				out.println("We do not offer microloans of that size");

				requestMicroloan(in, out);

				return;
			}

			out.println("What is the total you afford to pay back, interest-free, in 12 months?");
			String affordAmount = in.readLine();
			double affordAmountValue = Double.parseDouble(affordAmount);

			if (affordAmountValue >= requestedAmountValue) {

				out.println("Congratulations, your microloan request has been accepted!");

				// Customer can afford loan
				// TODO: add to customer bank account
				// TODO: make a note of when the customer needs to repay

			} else {

				out.println("In good conscience we cannot loan you the money for affordability reasons");

				return;
			}

		} catch (IOException r) {

			out.println(r);
		}
	}

}

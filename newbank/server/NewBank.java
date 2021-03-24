// NBS group branch change

package newbank.server;

import java.util.HashMap;
import java.util.Scanner;

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

	public synchronized boolean checkPassword(String password) {
		if (password.length() >= 7)
			return true;
		else {
			return false;
		}
	}

	//Rebecca's card - to be implemented. For now it does not check for Login.
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		//return null; ORIGINAL CODE TO UN-COMMENT LATER
		return new CustomerID(userName);
	}


	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {

		if (customers.containsKey(customer.getKey())) {

			switch(request) {

				case "1" :
					// List your accounts
					return showMyAccounts(customer);

				case "2" :
					// Create a new account
					return "CreateNewAccount Feature Incomplete";

				case "3" :
					// Transfer money between your accounts
					return "TransferMoney Feature Incomplete";

				case "4" :
					// Send money to another person
					return "SendMoney Feature Incomplete";

				case "5" :
					// Logout
					return "Logout Feature Incomplete";

				default :
					return "Invalid option. Please try again.";
			}
		}

		return "Account does not exist"; //if getKey() fails ?
	}

	private String showMyAccounts(CustomerID customer) {

		return (customers.get(customer.getKey())).accountsToString();
	}

}

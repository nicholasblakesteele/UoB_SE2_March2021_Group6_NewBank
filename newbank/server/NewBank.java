// NBS group branch change

package newbank.server;

import java.util.HashMap;
import java.util.Scanner;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
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

		//For showMyAccounts() testing purposes -- can be deleted later
		Customer tempCustomer = new Customer();
		tempCustomer.addAccount(new Account("Main", 9999.99));
		tempCustomer.addAccount(new Account("Checking", 9000.00));
		tempCustomer.addAccount(new Account("Savings", 999.99));
		customers.put("tempCustomer", tempCustomer);
	}

	public static NewBank getBank() {
		return bank;
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
			case "1" : return showMyAccounts(customer);
			default : return "Invalid Input. Please try again.";
			}
		}
		return "Account does not exist"; //if getKey() fails ?
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}

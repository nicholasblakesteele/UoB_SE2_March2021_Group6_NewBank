package newbank.server;


import java.util.HashMap;


public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private HashMap<String,String> passwords;
	public HashMap<String, Customer> getCustomersMap(){
	//public HashMap<String, Customer> getCustomers(){
		return customers;
	}

	public HashMap<String, String> getPasswordsMap() {
		return passwords;
	}

	NewBank() {
		customers = new HashMap<>();
		passwords = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		passwords.put("Bhagy", "1234");

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		passwords.put("Christina", "1234");

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
		passwords.put("John", "1234");

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
		if (password.length() < 7)
			return true;
		else {
			return false;
		}
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName) ){
			if (passwords.get(userName).equals(password)) {
				return new CustomerID(userName);
			}
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			default : return "FAIL";
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}

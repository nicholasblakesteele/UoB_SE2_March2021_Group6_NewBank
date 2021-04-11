package NewBank.newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString() + "\n"; //each string item prints on a new line
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}

}

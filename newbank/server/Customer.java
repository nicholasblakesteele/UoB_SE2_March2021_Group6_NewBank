package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String space = " ";
		int spaceCount = 0;
		String s = " ╭────────────────────── Accounts ───────────────────────╮\n";
		for(Account a : accounts) {
			//each string item prints on a new line
			spaceCount = 52 - a.toString().length();
			s +=  " │ " + a.toString() + " £" + space.repeat(spaceCount) + "│\n";
		}
		s += " ╰───────────────────────────────────────────────────────╯";
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}

}

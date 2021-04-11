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
		for (Account a : accounts) {
			//each string item prints on a new line
			spaceCount = 52 - a.toString().length();
			s += " │ " + a.toString() + " £" + space.repeat(spaceCount) + "│\n";
		}
		s += " ╰───────────────────────────────────────────────────────╯";
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public double showBalance(String accountName) {
		String s = "";
		double balance = 0.0;
		for (Account a : accounts) {
			s = a.toString();
			if (s.contains(accountName)) {
				balance = a.getBalance();
			}
		}
		return balance;
	}

	public void updateBalance(String accountName, double amount) {
		String s = "";
		for (Account a : accounts) {
			s = a.toString();
			if (s.contains(accountName)) {
				a.setBalance(amount);
			}
		}

	}

	public boolean accountExists(String accountName) {
		boolean accountFound = false;
		String s = "";
		for (Account a : accounts) {
			s = a.toString();
			if (s.contains(accountName)) {
					accountFound = true;
			}
			break;}

		return accountFound;
		}
	}




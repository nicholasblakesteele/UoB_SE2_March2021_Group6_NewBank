package newbank.server;

public class Account {
	
	private String accountName;
	private double openingBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public double getBalance(){
		return openingBalance;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setBalance( double num){
		openingBalance = num;
	}

}

// NBS group branch test

//3/18/21 push test Nina J. -- to delete later!
// Hello I'm announcing this is my branch now

package newbank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExampleClient extends Thread{

	private Socket server;
	private PrintWriter bankServerOut;
	private BufferedReader userInput;
	private Thread bankServerResponceThread;

	public ExampleClient(String ip, int port) throws UnknownHostException, IOException {

		server = new Socket(ip,port);
		userInput = new BufferedReader(new InputStreamReader(System.in));
		bankServerOut = new PrintWriter(server.getOutputStream(), true);

		bankServerResponceThread = new Thread() {

			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
			public void run() {
				try {
					while(true) {
						String response = bankServerIn.readLine();
						System.out.println(response);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		};
		bankServerResponceThread.start();
	}

	public void run() {

		printClientWelcomeMessage();

		while(true) {
			try {
				while(true) {
					String command = userInput.readLine();
					bankServerOut.println(command);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new ExampleClient("localhost",14002).start();
	}

	private void printClientWelcomeMessage() {

		System.out.println("\n");
		System.out.println("=========================================================");
		System.out.println("Welcome to the Group 6 Bank - YOUR international bank!");
		System.out.println("=========================================================");
		System.out.println("Please login to continue");
	}
}

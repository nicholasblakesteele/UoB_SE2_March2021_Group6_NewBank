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
	private Thread bankServerResponseThread;

	public ExampleClient(String ip, int port) throws UnknownHostException, IOException {

		server = new Socket(ip,port);
		userInput = new BufferedReader(new InputStreamReader(System.in));
		bankServerOut = new PrintWriter(server.getOutputStream(), true);

		bankServerResponseThread = new Thread() {

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
		bankServerResponseThread.start();
		// Changed position of printClientWelcomeMessage() to expected UI display order
		printClientWelcomeMessage();
	}

	public void run() {

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

		System.out.println(" _____                          ____  ______             _    ");
		System.out.println("|  __ \\                        / ___| | ___ \\           | |   ");
		System.out.println("| |  \\/_ __ ___  _   _ _ __   / /___  | |_/ / __ _ _ __ | | __");
		System.out.println("| | __| '__/ _ \\| | | | '_ \\  | ___ \\ | ___ \\/ _` | '_ \\| |/ /");
		System.out.println("| |_\\ \\ | | (_) | |_| | |_) | | \\_/ | | |_/ / (_| | | | |   < ");
		System.out.println(" \\____/_|  \\___/ \\__,_| .__/  \\_____/ \\____/ \\__,_|_| |_|_|\\_\\");
		System.out.println("                      | |                                     ");
		System.out.println("                      |_|                                     ");
		System.out.println(" ╭────────────────────────────────────────────────────────╮");
		System.out.println(" │ Welcome to the Group 6 Bank - YOUR international bank! │");
		System.out.println(" ╰────────────────────────────────────────────────────────╯");
		System.out.println("\n");

	}
}

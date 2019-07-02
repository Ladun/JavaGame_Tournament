package com.ladun.tournamentserver;

import java.util.Scanner;

public class TournamentServer {

	public static void main(String[] args) {
		Server server = new Server(8192);		
		server.start();
		
		boolean running = true;
		Scanner sc = new Scanner(System.in);
		
		String s;
		while(running) {
			s = sc.nextLine();
			switch(s) {
			case "stop":
			case "exit":
			case "shutdown":
				running = false;
				server.stopServer();
				break;
			case "list":
				System.out.println("------------Clinet List-------------");
				for(Client c : server.getClients()) {
					System.out.println("[" + c.getAddress().getHostAddress()+":"+c.getPort()+"] ID: "+c.getClientdID());
				}
				System.out.println("------------------------------------");
				break;
			case "packet":
				server.setPackingCatching(!server.isPackingCatching());
				
				System.out.println("Packet Catch : " + server.isPackingCatching());
				break;
			default:
				System.out.println("-----------No----------------");
			}
		}
	}
}

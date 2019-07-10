package com.ladun.tournamentserver;

import java.util.Scanner;

public class TournamentServer {

	public static void main(String[] args) {
		Server server = new Server(8192);		
		server.start();
		
		boolean running = true;
		Scanner sc = new Scanner(System.in);
		
		String s;
		String[] commands;
		while(running) {
			s = sc.nextLine();
			commands = s.split(" ");
			switch(commands[0]) {
			case "stop":
			case "exit":
			case "shutdown":
				running = false;
				server.stopServer();
				break;
			case "list":
				System.out.println("------------Client List-------------");
				for(Client c : server.getClients()) {
					System.out.println("[" + c.getAddress().getHostAddress()+":"+c.getPort()+"] ID: "+c.getClientdID());
				}
				System.out.println("------------------------------------");
				break;
			case "packet":
				if(commands.length == 1) {
					server.setPacketCatchType((byte)0x00);
					server.setPackingCatching(!server.isPackingCatching());
					System.out.println("Packet Catch : " + server.isPackingCatching());
				}else {
					try {
						byte b = Byte.parseByte(commands[1]);
						server.setPacketCatchType(b);
						server.setPackingCatching(true);
						System.out.println("Packet " + commands[1] + " Catch : " + server.isPackingCatching());
					} catch(NumberFormatException e) {
						NoneCommand(s);
					}
				}
				break;
			default:
				NoneCommand(s);
			}
		}
	}
	
	private static void NoneCommand(String s) {
		System.out.printf("-------\"%s\" is not a command----------\n",s);
		
	}
}

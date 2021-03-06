package com.ladun.tournamentserver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

public class TournamentServer {

	public static void main(String[] args) {
		Server server = new Server();	
		server.start();
		
		boolean running = true;
		Scanner sc = new Scanner(System.in);

		System.out.println("========================================================================");
		System.out.println("============================[ Server Start ]============================");
		System.out.println("========================================================================");
		ShowIpAddress();
		System.out.println("[Server Port] " + 8192);
		System.out.println("========================================================================");
		
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
			case "kick":{

				try {
					int _clientID = Integer.parseInt(commands[1]);
					if(_clientID < 1000 || !server.isClientExist(_clientID))
						return;	

					System.out.printf("[%s:%d] ID: %d kick\n",server.getClient(_clientID).getAddress().getHostAddress() , server.getClient(_clientID).getPort(), _clientID );	
					for(int i = 0 ; i < server.getClients().size(); i++) {
						if(server.getClients().get(i).getClientdID() == _clientID) {
							server.getClients().remove(i);
							break;
						}
					}
					server.clientDisconnect(_clientID);
					

				}
				catch(NumberFormatException e) {
					System.out.println("parameter is not a number");
				}
				break;
			}
			case "re_setting":{
				server.getSettingFile();
				break;				
			}
			default:
				NoneCommand(s);
			}
		}
		System.out.println("Server Stopped");
		
	}
	
	private static void NoneCommand(String s) {
		System.out.printf("-------\"%s\" is not a command----------\n",s);
		
	}
	private static void ShowIpAddress() {
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	System.out.println("[Server IP  ] " + inetAddress.getHostAddress().toString());
		            }
		        }
		    }
		}
		catch (SocketException ex) {}
	}
}

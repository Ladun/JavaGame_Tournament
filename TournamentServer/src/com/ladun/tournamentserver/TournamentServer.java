package com.ladun.tournamentserver;

public class TournamentServer {

	public static void main(String[] args) {
		Server server = new Server(8192);		
		server.start();
		
		
	}
}

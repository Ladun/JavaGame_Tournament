package com.ladun.tournamentserver;

public class Timeout implements Runnable{

	private Server server;
	public Timeout(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		double startTime = System.currentTimeMillis();
		int next = 1;
		double nowTime;		

		
		while(server.isListening()) {
			nowTime = (System.currentTimeMillis() - startTime ) / 1000;
			
			if(nowTime >= next) {
				next++;
				for(int i = 0; i < server.getClients().size();i++) {
					Client c = server.getClients().get(i);
					c.setTimeSinceCheck(c.getTimeSinceCheck() + 1);
					if(c.getTimeSinceCheck() >= 5) {
						c.setTimeSinceCheck(0);
						if(c.isResponed()) {
							c.setChekTime(System.currentTimeMillis());
							c.setResponed(false);
							server.send(new byte[] {(byte)0x05}, c.getAddress(), c.getPort());
						}
						else {
							//remove
							int clientID = c.getClientdID();
							System.out.println("Client: " + clientID + " does not responded");
							server.getClients().remove(i);
							i--;
							server.clientDisconnect(clientID);
						}
					}
				}
			}
		}
	}

}

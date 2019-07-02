package com.ladun.tournamentserver;

import java.net.InetAddress;

public class Client {
	private InetAddress address;
	private int port;
	private int clientdID;
	
	private int x,y,z,angle;
	
	public Client(InetAddress address,int port,int clientID) {
		this.address = address;
		this.port = port;
		this.clientdID = clientID;
	}
	public String getData() {
		StringBuilder sb =new StringBuilder();
		sb.append(clientdID);
		sb.append(",");
		sb.append(x);
		sb.append(",");
		sb.append(y);
		sb.append(",");
		sb.append(z);
		sb.append(",");
		sb.append(angle);
		
		return sb.toString();
	}

	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}

	public int getClientdID() {
		return clientdID;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	
}

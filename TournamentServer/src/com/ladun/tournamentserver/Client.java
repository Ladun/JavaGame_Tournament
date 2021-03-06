package com.ladun.tournamentserver;

import java.net.InetAddress;

import com.ladun.tournamentserver.item.Item;

public class Client {
	
	private String nickname;
	
	private InetAddress address;
	private int port;
	private int clientdID;
	
	private int money;
	
	private int x,y,z,angle;
	private int anim, animType;	

	private byte teamNumber; //
	private int targetMapIndex;
	
	private int health;
	
	private int currentMapIndex;
	
	private Item[] items;
	//------------------------------------
	private double chekTime;
	private int timeSinceCheck;
	private boolean responed = true;
	
	public Client(InetAddress address,int port,int clientID,String nickname) {
		this.address = address;
		this.port = port;
		this.clientdID = clientID;
		this.nickname = nickname;
		items = new Item[6];
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
	public int getAnim() {
		return anim;
	}
	public void setAnim(int anim) {
		this.anim = anim;
	}
	public int getAnimType() {
		return animType;
	}
	public void setAnimType(int animType) {
		this.animType = animType;
	}
	public boolean isResponed() {
		return responed;
	}
	public void setResponed(boolean responed) {
		this.responed = responed;
	}
	public double getChekTime() {
		return chekTime;
	}
	public void setChekTime(double chekTime) {
		this.chekTime = chekTime;
	}
	public int getTimeSinceCheck() {
		return timeSinceCheck;
	}
	public void setTimeSinceCheck(int timeSinceCheck) {
		this.timeSinceCheck = timeSinceCheck;
	}

	public byte getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(byte teamNumber) {
		this.teamNumber = teamNumber;
	}


	public int getTargetMapIndex() {
		return targetMapIndex;
	}

	public void setTargetMapIndex(int targetMapIndex) {
		this.targetMapIndex = targetMapIndex;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}


	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCurrentMapIndex() {
		return currentMapIndex;
	}

	public void setCurrentMapIndex(int currentMapIndex) {
		this.currentMapIndex = currentMapIndex;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Item[] getItems() {
		return items;
	}

	
}

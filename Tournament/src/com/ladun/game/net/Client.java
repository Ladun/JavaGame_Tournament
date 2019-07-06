package com.ladun.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.NetworkTransform;
import com.ladun.game.objects.Player;
import com.ladun.game.util.BinaryWritter;

public class Client {
	private final static byte[] PACKET_CLIENT_HEADER = new byte[] { 0x40,0x40};
	private final static byte PACKET_TYPE_CONNECT = 0x01;
	private final static byte PACKET_TYPE_DISCONNECT = 0x02;
	public final static byte PACKET_TYPE_VALUECHANGE = 0x03; 
	public final static byte PACKET_TYPE_OBJECTSPAWN = 0x04; 
	
	public enum Error{
		NONE, INVALID_HOST,SOCKET_EXCEPTION
	}
	
	private String ipAddress;
	private int port;
	private Error errorCode = Error.NONE;
	private int clientID;
	
	private Thread listenThread;
	private boolean listening = false;	
	private InetAddress serverAddress;	
	private DatagramSocket socket;	

	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receiveDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	private GameManager gm;
	
	private BinaryWritter bw = new BinaryWritter();	
	private StringBuilder sb = new StringBuilder();
	
	//-------------------------------------------------------------------	
	/**
	 * 
	 * @param host
	 * 			  Eg. 192.168.1.1:5000
	 */
	public Client(String host,GameManager gm) {
		String[] parts = host.split(":");
		if(parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		
		this.ipAddress = parts[0];
		try {
			this.port = Integer.parseInt(parts[1]);
		}catch(NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		this.gm = gm;
	}	
	/**
	 * 
	 * @param host
	 * 			  Eg. 192.168.1.1
	 * @param port
	 * 			  Eg. 5000
	 */
	public Client(String host, int port,GameManager gm) {
		this.ipAddress= host;
		this.port = port;
		this.gm = gm;
	}
	
	//-------------------------------------------------------------------
	public boolean connect() {
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorCode = Error.SOCKET_EXCEPTION;
			return false;
		}
		
		
		// Send connection packet to server
		sb.setLength(0);
		bw.clear();
		bw.write(PACKET_TYPE_CONNECT);
		send(bw.getBytes());

		// Listening Thread Start
		listening = true;		
		listenThread = new Thread(() -> listen(), "Server-ListenThread");		
		listenThread.start();
		
		return true;
	}


	private void listen() {
		while(listening) {
			DatagramPacket packet = new DatagramPacket(receiveDataBuffer, MAX_PACKET_SIZE);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();				
			} 
			process(packet);
		}
	}
	
	private void process(DatagramPacket packet) {
		byte[] data = packet.getData();
		if(!packet.getAddress().equals(serverAddress) || packet.getPort() != port)
			return;		

		if(data[2] == 0x04)
			dumpPacket(packet);
		
		if(data[0] == 0x41 && data[1] == 0x41) {
			String[] messages = new String(data,0,packet.getLength()).split(":");	
			String[] netArgs;
			
			switch(data[2]) {
			// Connection -> Receive Other Player Connection
			// ,Or player connect and spawn other Player
			// Server -> Client Packet : [header type: clientID]
			case 0x01 :  //type				
				
				sb.setLength(0);
				sb.append("Player");
				sb.append(messages[1].trim());
				
				((GameScene)gm.getScene("InGame")).addPlayer(sb.toString(),0,0,	false);
				
				break;
			// Disconnection
			// Server -> Client Packet : [header type: clientID]	
			case 0x02 :  //type			
				//TODO: Ohter Player disconnect server, delete other Player 	
				sb.setLength(0);
				sb.append("Player");
				sb.append(messages[1].trim());
				
				((GameScene)gm.getScene("InGame")).removePlayer(sb.toString());
				break;
			// Value Change
			// Server -> Client Packet : [header type: clientID : ValueType, value,....]
			case 0x03: //type			
				netArgs = messages[2].split(",");
				if(netArgs[0].toCharArray()[0] == 0x10)
				{
					this.clientID = Integer.parseInt(messages[1].trim());
					
					sb.setLength(0);
					sb.append("Player");
					sb.append(this.clientID);
					
					Player _p = ((GameScene)gm.getScene("InGame")).addPlayer(sb.toString(), true);					
					((GameScene)gm.getScene("InGame")).getCamera().setTargetTag(sb.toString());
					
					sb.setLength(0);
					bw.clear();
					bw.write(PACKET_TYPE_OBJECTSPAWN);
					sb.append(":");
					sb.append(this.clientID);
					bw.write((sb.toString()).getBytes());
					send(bw.getBytes());
					
					((NetworkTransform)_p.findComponent("netTransform")).packetSend(gm);; 
				}
				else if(netArgs[0].toCharArray()[0] == 0x11) {	
					// ValueType, x, y, z, angle
					
					sb.setLength(0);
					sb.append("Player");
					sb.append(messages[1].trim());
					Player _p = (Player)gm.getObject(sb.toString());
					if(_p == null)
						return;

					NetworkTransform nt = (NetworkTransform)_p.findComponent("netTransform");
					
					nt.setInfo(
							Float.parseFloat(netArgs[1]),
							Float.parseFloat(netArgs[2]),
							Float.parseFloat(netArgs[3]),
							Float.parseFloat(netArgs[4]));
				}

				break;
			// Object Spawn--------------------------------------------------------
			// Client -> Server Packet : [header type: clientID (: objectName,parameter,.....)]
			case 0x04:
				if(messages.length >= 3) {
					netArgs = messages[2].split(",");
					if(netArgs[0].equals("bullet")) {
	
						sb.setLength(0);
						sb.append("Player");
						sb.append(messages[1].trim());
						Player _p = (Player)gm.getObject(sb.toString());
						if(_p == null)
							return;
						
						_p.Shoot(null);
					}				
				}
				break;
				
			}
		}
	}
	
	public void disconnect() {
		
		bw.clear();
		sb.setLength(0);
		bw.write(PACKET_TYPE_DISCONNECT);
		sb.append(":");
		sb.append(clientID);
		bw.write((sb.toString()).getBytes());
		send(bw.getBytes());	
		
		/*
		byte[] data = new byte[] {0x00,0x00};
		DatagramPacket packet;
		try {
			packet = new DatagramPacket(data, data.length, InetAddress.getByName("127.0.0.1"),socket.getPort());
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		socket.close();*/
	}
	/*
	 * PACKET_HEADER�� send �Լ����� �ڵ����� �ٿ���
	 */
	public void send(byte b) {
		send(new byte[] {b});
	}
	
	public void sendValue(byte[] _data) {
		bw.clear();
		sb.setLength(0);
		bw.write(PACKET_TYPE_VALUECHANGE);
		sb.append(":");
		sb.append(clientID);
		sb.append(":");
		bw.write((sb.toString()).getBytes());
		bw.write(_data);
		send(bw.getBytes());
	}
	
	public void send(byte type, Object[] _data) {
		bw.clear();
		sb.setLength(0);
		bw.write(type);
		sb.append(":");
		sb.append(clientID);
		sb.append(":");
		for(int i = 0 ;i < _data.length;i++) {
			sb.append(_data[i]);
			if(i != _data.length -1)
				sb.append(',');
		}
		bw.write((sb.toString()).getBytes());
		send(bw.getBytes());
	}
	
	public void send(byte[] _data) {
		assert(socket.isConnected());
		
		bw.clear();
		bw.write(PACKET_CLIENT_HEADER);
		bw.write(_data);
		
		byte[] data = bw.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//-------------------------------------------------------------------
	private void dumpPacket(DatagramPacket packet) {
		byte[] data = packet.getData();
		InetAddress address = packet.getAddress();
		int port= packet.getPort();

		System.out.println("-----------------------------------------------");
		System.out.println("PACKET:");
		System.out.println("\t" + address.getHostAddress() + ":" + port);
		System.out.println("\tConetents:");
		System.out.print("\t\t");
		for(int i = 0 ; i < packet.getLength(); i++) {
			System.out.printf("%4x",data[i]);
			if((i +1) % 8 == 0) {
				System.out.print("\n\t\t");
			}
		}
		System.out.println("\n\t\t" + new String(data,0,packet.getLength()));
		System.out.println();
		System.out.println("-----------------------------------------------");
		
	}
	public void setHost(String host) {
		String[] parts = host.split(":");
		if(parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		
		this.ipAddress = parts[0];
		try {
			this.port = Integer.parseInt(parts[1]);
		}catch(NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
	}
	public void setHost(String host, int port) {
		this.ipAddress= host;
		this.port = port;
	}
	
	public Error getErrorCode() {
		return errorCode;
	}
}
package com.ladun.tournamentserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.ladun.tournamentserver.util.BinaryWritter;

public class Server {

	private final static byte[] PACKET_SERVER_HEADER = new byte[] { 0x41,0x41};
	private final static byte PACKET_VALUETYPE_CLIENTID = 0x10;
	
	private int port;
	private Thread listenThread;
	private boolean listening = false;
	private DatagramSocket socket;
	
	private ArrayList<Client> clients = new ArrayList<Client>();
	private int clientID = 1000;
	

	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receiveDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	private BinaryWritter bw = new BinaryWritter();
	private StringBuilder sb = new StringBuilder();
	
	private long stTime;
	
	private boolean packingCatching = false;
	private byte packetCatchType = 0x00;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("--Start--------");
		
		
		listening = true;
		
		listenThread = new Thread(() -> listen(), "Server-ListenThread");		
		listenThread.start();
		
		
		stTime = System.currentTimeMillis();
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
		InetAddress address = packet.getAddress();
		int port= packet.getPort();
		
		if(packingCatching)
			dumpPacket(packet);
		
		if(data[0] == 0x40 && data[1] == 0x40) {
			String[] messages = new String(data,0,packet.getLength()).split(":");
			int _clientID = 0;
			
			switch(data[2]) {
			// Connection-----------------------------------------------------------
			// Client -> Server Packet : [header type]
			case 0x01 : 
				clients.add(new Client(address,port,clientID));
				
				sb.setLength(0);
				bw.clear();
				bw.write((byte)0x03); //0x03 Value_Change -> �� ����, clientID�� ������
				sb.append(":");
				sb.append(clientID);
				sb.append(":");
				bw.write((sb.toString()).getBytes()); // ������:clientID:clientID
				bw.write(PACKET_VALUETYPE_CLIENTID);
				send(bw.getBytes(),address,port);
				
				System.out.printf("[%s:%d] ID: %d Connect\n",address.getHostAddress() , port, clientID );			
				clientID++;
				break;
			// Disconnection--------------------------------------------------------
			// Client -> Server Packet : [header type: clientID]
			case 0x02:
				_clientID = Integer.parseInt(messages[1].trim());
				
				if(_clientID < 1000)
					break;		
				
				for(int i = 0 ; i < clients.size(); i++) {
					if(clients.get(i).getClientdID() == _clientID) {
						clients.remove(i);
						break;
					}
				}
				sb.setLength(0);
				bw.clear();
				bw.write((byte)0x02);
				sb.append(":");
				sb.append(_clientID);
				bw.write((sb.toString()).getBytes());
				
				sendToAllClients(bw.getBytes());

				System.out.printf("[%s:%d] ID: %d Disconnect\n",address.getHostAddress() , port, _clientID );		
				
				break;
			// Value Change--------------------------------------------------------
			// Client -> Server Packet : [header type: clientID : ValueType, value,....]
			case 0x03:
				String[] netArgs = messages[2].split(",");
				if(netArgs[0].toCharArray()[0] == 0x11) {
					// ValueType, x, y, z, angle
					
					_clientID = Integer.parseInt(messages[1].trim());

					if(_clientID < 1000)
						break;

					sb.setLength(0);
					bw.clear();
					bw.write((byte)0x03);
					sb.append(":");
					sb.append(_clientID);
					sb.append(":");
					sb.append(messages[2]);
					bw.write((sb.toString()).getBytes());			
					

					sendToAllClients(bw.getBytes(), _clientID); 
					
				}
				break;
			// Object Spawn--------------------------------------------------------
			// Client -> Server Packet : [header type: clientID (: objectName,parameter,.....)]
			case 0x04:
				if(messages.length == 2) {		// Player Spawn		
					_clientID = Integer.parseInt(messages[1].trim());
	
					if(_clientID < 1000)
						break;
					
					sb.setLength(0);
					bw.clear();
					bw.write((byte)0x01);
					sb.append(":");
					sb.append(_clientID);
					bw.write((sb.toString()).getBytes());
					// ���� ���ο� �÷��̾ ���� �Ǿ��ٰ� �������� client���� ������ ����
					sendToAllClients(bw.getBytes(), _clientID); 
					
					//���� ���� ������ �÷��̾�� ������ �÷��̾���� ������ �Ѹ�
					for(Client _c : clients) {
						if(_c.getClientdID() != _clientID)
						{
							sb.setLength(0);
							bw.clear();
							bw.write((byte)0x01);
							sb.append(":");
							sb.append(_c.getClientdID());
							bw.write((sb.toString()).getBytes());
							send(bw.getBytes(),address,port);
						}
					}
				}
				else {
					System.out.println(messages[2]);
					_clientID = Integer.parseInt(messages[1].trim());
				
					if(_clientID < 1000)
						break;

					sb.setLength(0);
					bw.clear();
					bw.write((byte)0x04);
					sb.append(":");
					sb.append(messages[1]);
					sb.append(":");
					sb.append(messages[2]);
					bw.write((sb.toString()).getBytes());
					
					
					sendToAllClients(bw.getBytes(),_clientID);
				}
				
				break;
				
			}
		}
		
	}
	
	public void send(byte[] _data, InetAddress address, int port) {
		assert(socket.isConnected());
		bw.clear();
		bw.write(PACKET_SERVER_HEADER);
		bw.write(_data);

		byte[] data = bw.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	/*
	 * ����� �ذ��
	 * �ڱ��ڽſ��� ��Ŷ�� ������ receive�� ���߰� �� ������
	 */
	public void stopServer() {
		this.listening = false;
		
		try {
			send("stop".getBytes(),InetAddress.getByName("localhost"),port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.close();
	}
	
	//--------------------------------------------------------------------------
	
	// Send to all clients
	public void sendToAllClients(byte[] data) {
		for(Client c : clients) {
			send(data,c.getAddress(),c.getPort());
		}
	}
	
	
	// Send to all clients except clients with the same clientID
	public void sendToAllClients(byte[] data,int clientID) {
		for(Client c : clients) {
			if(c.getClientdID() == clientID)
				continue;
			
			send(data,c.getAddress(),c.getPort());
		}
	}
	
	private void dumpPacket(DatagramPacket packet) {
		byte[] data = packet.getData();
		InetAddress address = packet.getAddress();
		int port= packet.getPort();

		if(packetCatchType != 0x00) 
			if(packetCatchType != data[2])
				return;
		
		System.out.println("-----------------------------------------------");
		System.out.printf("PACKET: %d\n",(System.currentTimeMillis() -stTime)/1000);
		System.out.printf("\t%s:%d\n" , address.getHostAddress(), port);
		System.out.println("\tConetents:");
		System.out.print("\t\t");
		for(int i = 0 ; i < packet.getLength(); i++) {
			System.out.printf("%4x",data[i]);
			if((i +1) % 8 == 0) {
				System.out.print("\n\t\t");
			}
		}
		System.out.printf("\n\t\t%s\n",new String(data,0,packet.getLength()));
		System.out.println();
		System.out.println("-----------------------------------------------");
		
	}
	//--------------------------------------------------------------------------
	public boolean isListening() {
		return listening;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}
	
	public Client getClient(int _clientID) {
		for(Client c : clients) {
			if(c.getClientdID() == _clientID) {
				return c;
			}
		}
		return null;
	}

	public boolean isPackingCatching() {
		return packingCatching;
	}

	public void setPackingCatching(boolean packingCatching) {
		this.packingCatching = packingCatching;
	}

	public void setPacketCatchType(byte packetCatchType) {
		this.packetCatchType = packetCatchType;
	}
	
	
	
	
}
package com.ladun.tournamentserver.util;

import java.util.ArrayList;

public class BinaryWritter {

	private ArrayList<Byte> byteList = new ArrayList<Byte>();
	
	
	
	public void write(byte b) {
		byteList.add(b);
	}
	public void write(byte[] data) {
		for(int i = 0; i < data.length;i++)
			byteList.add(data[i]);
	}
	public byte[] getBytes() {
		byte[] bytes = new byte[byteList.size()];
		
		for(int i = 0; i < bytes.length;i++) {
			bytes[i] = byteList.get(i);
		}
		
		byteList.clear();
		return bytes;
	}

	public void clear() {
		byteList.clear();
	}
}

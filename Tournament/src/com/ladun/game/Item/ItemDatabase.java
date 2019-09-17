package com.ladun.game.Item;

import java.util.ArrayList;

import com.ladun.game.GameManager;
import com.ladun.game.net.Client;

public class ItemDatabase {

	private ArrayList<Item> itemList = new ArrayList<Item>();

	public void init(GameManager gm) {
		
		gm.getClient().send(Client.PACKET_TYPE_ITEM,new Object[] {});
	}	

	public void AddItem(int id,int price, int types, String name, int[] options) {
		
		itemList.add(new Item(id, price, types,name,options));
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}
}

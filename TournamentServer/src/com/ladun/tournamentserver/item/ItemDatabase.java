package com.ladun.tournamentserver.item;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ItemDatabase {

	private ArrayList<Item> itemList = new ArrayList<Item>();
	
	public ItemDatabase() {	
		JsonParser parser = new JsonParser();

		try {
			 
			Object obj = parser.parse(new FileReader(".//ItemData.json"));
	 
			JsonArray jsonArray = (JsonArray) obj;
			//JsonArray itemList = jsonObject.getAsJsonArray();
			for(int i = 0; i < jsonArray.size();i++) {
				JsonObject jsonObj = (JsonObject) jsonArray.get(i);
				
				String[] types = new String[jsonObj.get("Types").getAsJsonArray().size()];
				for(int t = 0; t < types.length;t++)
					types[t] = jsonObj.get("Types").getAsJsonArray().get(t).getAsString();
				int[] options = new int[jsonObj.get("Options").getAsJsonArray().size()];
				for(int t = 0; t < types.length;t++)
					options[t] = jsonObj.get("Options").getAsJsonArray().get(t).getAsInt();
				
				if(options.length != types.length)
					continue;
				
				AddItem(jsonObj.get("ID").getAsInt(),jsonObj.get("Price").getAsInt(),types,jsonObj.get("Name").getAsString(),options);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void AddItem(int id,int price, String[] types, String name, int[] options) {
		int type = 0;
		for(int i = 0; i < types.length;i++) {
			try {
				type = type | Item.Type.valueOf(types[i]).getValue();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
				return;
			}
		}
		itemList.add(new Item(id,price, type,name,options));
	}

	public Item getItem(int itemID) {
		for(Item _item : itemList) {
			if(_item.getID() == itemID)
				return _item;
		}
		return null;
	}
	public ArrayList<Item> getItemList() {
		return itemList;
	}

}

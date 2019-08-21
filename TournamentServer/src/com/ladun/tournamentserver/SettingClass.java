package com.ladun.tournamentserver;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SettingClass {
	
	private int roundCount;
	
	public SettingClass(String jsonPath) {	
		JsonParser parser = new JsonParser();

		try {
			 
			Object obj = parser.parse(new FileReader(jsonPath));
	 
			JsonObject jsonObject = (JsonObject) obj;
			roundCount = jsonObject.get("ROUND_COUNT").getAsInt();
	 
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getRoundCount() {
		return roundCount;
	}
	
	
}

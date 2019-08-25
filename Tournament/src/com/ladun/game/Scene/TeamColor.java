package com.ladun.game.Scene;

public enum TeamColor {
	TEAM1(0xffd83c3c),TEAM2(0xffd8953c),TEAM3(0xffd8d13c),TEAM4(0xff74d83c),
	TEAM5(0xff74d83c),TEAM6(0xff3c6ed8),TEAM7(0xff843cd8),TEAM8(0xffc53cd8);
	
	private final int color;
	private TeamColor(int color) {
		this.color = color;
	}
	public int getValue() {
		return color;
	}
	
	public static int getColor(int i) {
		return values()[i].getValue();
	}
}

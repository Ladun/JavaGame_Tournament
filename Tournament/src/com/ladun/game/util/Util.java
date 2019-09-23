package com.ladun.game.util;

public class Util {
	public static float lerp(float src, float dst, float percent) {
		if(percent < 0)
			percent = 0;
		else if (percent > 1)
			percent = 1;
		
		return src + (dst - src) * percent;
	}
	
	public static float distance(float stX, float stY, float edX, float edY) {
		float dx = edX - stX;
		float dy = edY - stY;
		return (float)Math.sqrt(dx * dx  + dy * dy);
	}
	
	public static float angle360(float angle) {
		return Math.abs(angle  - 360) % 360;
	}
}

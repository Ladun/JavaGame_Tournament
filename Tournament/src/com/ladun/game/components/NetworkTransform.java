package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;
import com.ladun.game.util.BinaryWritter;

public class NetworkTransform extends Component{

	private final static byte PACKET_VALUETYPE_PLAYERTINFO = 0x11;//packet|value type|player transform information
	
	private final static float PACKET_SEND_TIME = 1/3f; 
	
	private float srcX, srcY, srcZ;
	private float srcAngle;
	private float dstX, dstY ,dstZ;
	private float dstAngle;
	
	private boolean localPlayer;
	private float time;
	
	private StringBuilder sb = new StringBuilder();
	private BinaryWritter bw = new BinaryWritter();
	
	
	public NetworkTransform(GameObject parent,boolean localPlayer)
	{
		this.parent = parent;
		this.tag = "netTransform";	
		this.localPlayer = localPlayer;
		
		srcX = parent.getPosX();
		srcY = parent.getPosY();
		srcZ = parent.getPosZ();
		srcAngle = parent.getAngle();
		
		dstX = srcX;
		dstY = srcY;
		dstZ = srcZ;
		dstAngle = srcAngle;

	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		time += dt;
		
		
		if(time > PACKET_SEND_TIME)
		{
			if(localPlayer)
				packetSend(gm);
		}
		
		if(!localPlayer) {
			parent.setPosX(lerp(srcX,dstX,time / PACKET_SEND_TIME));
			parent.setPosY(lerp(srcY,dstY,time / PACKET_SEND_TIME));
			parent.setPosZ(lerp(srcZ,dstZ,time / PACKET_SEND_TIME));
			parent.setAngle(lerp(srcAngle,dstAngle,time / PACKET_SEND_TIME));		
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
	}
	
	public void packetSend(GameManager gm) {		
		bw.clear();
		sb.setLength(0);
		bw.write(PACKET_VALUETYPE_PLAYERTINFO);
		sb.append(",");
		sb.append(parent.getPosX());
		sb.append(",");
		sb.append(parent.getPosY());
		sb.append(",");
		sb.append(parent.getPosZ());
		sb.append(",");
		sb.append(parent.getAngle());
		bw.write((sb.toString()).getBytes());
		
		gm.getClient().sendValue(bw.getBytes());
	}

	private float lerp(float src, float dst, float percent) {
		if(percent < 0)
			percent = 0;
		else if (percent > 1)
			percent = 1;
		
		return src + (dst - src) * percent;
	}
	
	public void setInfo(float x, float y, float z, float angle) {	
		time -= PACKET_SEND_TIME;
		this.srcX = dstX;
		this.srcY = dstY;
		this.srcZ = dstZ;
		this.srcAngle = dstAngle;
		
		this.dstX = x;
		this.dstY = y;
		this.dstZ = z;
		this.dstAngle = angle;
	}
}

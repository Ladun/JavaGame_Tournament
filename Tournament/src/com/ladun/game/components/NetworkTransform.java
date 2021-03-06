package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.GameManager;
import com.ladun.game.net.Client;
import com.ladun.game.objects.Entity;
import com.ladun.game.util.Util;

public class NetworkTransform extends Component{

	private final static byte PACKET_VALUETYPE_PLAYERTINFO = 0x11;//packet|value type|player transform information
	
	private final static float PACKET_SEND_TIME = 1/10f; 
	
	
	private float srcX, srcY, srcZ;
	private float srcAngle;
	private float dstX, dstY ,dstZ;
	private float dstAngle;
	
	private int anim;
	private int animType;
	
	private boolean localPlayer;
	private float time;
	
	
	public NetworkTransform(Entity parent,boolean localPlayer)
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
	public void update(GameContainer gc, GameManager gm) {
		
		time += Time.DELTA_TIME;
		
		
		if(time > PACKET_SEND_TIME)
		{
			if(localPlayer) {
				time -= PACKET_SEND_TIME;
				packetSend(gm);
			}
		}
		
		if(!localPlayer) {
			parent.setPosX(Util.lerp(srcX,dstX,time / PACKET_SEND_TIME));
			parent.setPosY(Util.lerp(srcY,dstY,time / PACKET_SEND_TIME));
			parent.setPosZ(Util.lerp(srcZ,dstZ,time / PACKET_SEND_TIME));
			parent.setAngle(Util.lerp(srcAngle,dstAngle,time / PACKET_SEND_TIME));	
			((Entity)parent).setAnim(anim);
			((Entity)parent).setAnimType(animType);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
	}
	//----------------------------------------------------------------------------------------
	public void packetSend(GameManager gm) {	
		
		gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE, new Object[] {
				(char)PACKET_VALUETYPE_PLAYERTINFO,
				parent.getPosX(),parent.getPosY(),parent.getPosZ(),parent.getAngle(),
				(int)((Entity)parent).getAnim(),((Entity)parent).getAnimType()});
		
	}

	
	public void setInfo(float x, float y, float z, float angle,int anim,int animType) {	
		time = 0;
		this.srcX = dstX;
		this.srcY = dstY;
		this.srcZ = dstZ;
		this.srcAngle = dstAngle;
		
		this.dstX = x;
		this.dstY = y;
		this.dstZ = z;
		this.dstAngle = angle;
		
		this.anim = anim;
		this.animType = animType;
	}
	//----------------------------------------------------------------------------------------

	public float getDstAngle() {
		return dstAngle;
	}

	public void setDstAngle(float dstAngle) {
		this.dstAngle = dstAngle;
	}
	
}

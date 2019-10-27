package com.ladun.game.Item;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.TeamColor;
import com.ladun.game.net.Client;
import com.ladun.game.objects.UI.Button;

public class ShopSlot {
	public static final int TOP_GAP = 15;
	public static final int GAP = 8;
	public static final int BUTTON_HEIGHT = 32;
	public static final int STRING_SIZE = 15;
	
	private int id;
	private Item item;
	private int posX,posY;
	private Button parchaseButton;
	private boolean active;
	
	private Shop sh;
	
	public ShopSlot(Shop sh,int id,Item item,int posX,int posY) {
		this.sh = sh;
		parchaseButton = new Button(0,0, Item.IMAGE_SIZE, BUTTON_HEIGHT, "shopSlot_Button");
		setItem(id,item, posX, posY);
		
	}
	
	public void update(GameContainer gc, GameManager gm) {
	
		parchaseButton.update(gc, gm);
		if(parchaseButton.isReleased()) {
			if(gm.getMoney() >= item.getPrice()) {
				int itemSlotIndex = sh.getGs().getLocalPlayer().emptyItemSlot();
				if(itemSlotIndex == -1) {
					// TODO: 아이템 창이 꽉참
					gm.getAnnounce().addString("아이템을 더 이상 구매할 수 없습니다.",0xff000000);
					gm.getAnnounce().show(3);
				}else {
					gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE, new Object[] {(char)0x20, item.getID(),itemSlotIndex,(char)0x00});
					sh.getGs().getLocalPlayer().getItems()[itemSlotIndex] = item;
					sh.getGs().getLocalPlayer().revival();
					sh.getGs().getLocalPlayer().setMana(sh.getGs().getLocalPlayer().getMaxMana());
				}
					
			}else {
				// TODO: 돈이 부족함

				gm.getAnnounce().addString("돈이 부족합니다.",0xff000000);
				gm.getAnnounce().show(3);
			}
		}
		
		if(gc.getInput().getMouseX() >= posX  && gc.getInput().getMouseX() <= posX + GAP * 2 + Item.IMAGE_SIZE &&
				gc.getInput().getMouseY() >= posY  && gc.getInput().getMouseY() <= posY + GAP + TOP_GAP + Item.IMAGE_SIZE) {
			sh.getGs().tooltipSetting(item,id,Tooltip.Type.Shop);
		}
		else {
			if(sh.getGs().getTooltip().getType() == Tooltip.Type.Shop)
				if(sh.getGs().getTooltip().getContentID() == id)
					sh.getGs().getTooltip().setActive(false);
		}
	}
	public void render(GameContainer gc, Renderer r) {
		r.setzDepth(Renderer.LAYER_UI);
		// background Render
		r.drawImage(gc.getResourceLoader().getImage("shopSlot"), posX , posY, 0);
		// item image Render
		item.render(gc, r, posX  + GAP, posY + TOP_GAP);
		// Price Render
		String priceString = item.getPrice() +"";
		r.drawText(priceString, posX + 35 - priceString.length() * 6 , posY + TOP_GAP + GAP + Item.IMAGE_SIZE  , 0xff000000);
		// Buy Button Render
		parchaseButton.render(gc, r);
	}
	public void setItem(int id,Item item,int posX, int posY) {

		active = true;
		this.id =id;
		this.item = item;
		this.posX = posX;
		this.posY = posY;
		parchaseButton.setPosX(posX + GAP);
		parchaseButton.setPosY(posY + GAP * 2 + TOP_GAP + STRING_SIZE + Item.IMAGE_SIZE);
		
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	public static int getWidth() {
		return GAP * 2 + Item.IMAGE_SIZE;
	}
	public static int getHeight() {
		return TOP_GAP + GAP * 3 + STRING_SIZE + BUTTON_HEIGHT + Item.IMAGE_SIZE;
	}
}


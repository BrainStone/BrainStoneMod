package mods.brainstonemod.templates;

import mods.brainstonemod.guis.GuiBrainStoneTrigger;

public class BrainStoneButton {
	public boolean inactive, down, invisible;
	public final int ID;
	public final int xSize, ySize;
	public final int xPos, yPos;
	public final int xDownPos, yDownPos;
	public final int xInactivePos, yInactivePos;
	public int triggerXmin, triggerYmin, triggerXmax, triggerYmax;

	public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos) {
		this(ID, xSize, ySize, xPos, yPos, 0, 0);
	}

	public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos,
			boolean invisible) {
		this(ID, xSize, ySize, xPos, yPos, 0, 0, invisible);
	}

	public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos,
			int xDownPos, int yDownPos) {
		this(ID, xSize, ySize, xPos, yPos, xDownPos, yDownPos, 0, 0, false);
	}

	public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos,
			int xDownPos, int yDownPos, boolean invisible) {
		this(ID, xSize, ySize, xPos, yPos, xDownPos, yDownPos, 0, 0, invisible);
	}

	public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos,
			int xDownPos, int yDownPos, int xInactivePos, int yInactivePos) {
		this(ID, xSize, ySize, xPos, yPos, xDownPos, yDownPos, xInactivePos,
				yInactivePos, false);
	}

	public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos,
			int xDownPos, int yDownPos, int xInactivePos, int yInactivePos,
			boolean invisible) {
		this.ID = ID;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xDownPos = xDownPos;
		this.yDownPos = yDownPos;
		this.xInactivePos = xInactivePos;
		this.yInactivePos = yInactivePos;

		inactive = false;
		down = false;
		this.invisible = invisible;

		triggerXmin = this.xPos;
		triggerYmin = this.yPos;
		triggerXmax = this.xPos + this.xSize;
		triggerYmax = this.yPos + this.ySize;
	}

	public BrainStoneButton changeTriggerBox(int triggerXmin, int triggerYmin,
			int triggerXmax, int triggerYmax) {
		this.triggerXmin = triggerXmin;
		this.triggerYmin = triggerYmin;
		this.triggerXmax = triggerXmax;
		this.triggerYmax = triggerYmax;

		return this;
	}

	public void render(GuiBrainStoneTrigger gui, int x, int y) {
		if (!invisible) {
			if (down && (!inactive)) {
				gui.drawTexturedModalRect(xPos + x, yPos + y, xDownPos,
						yDownPos, xSize, ySize);
			} else if (inactive) {
				gui.drawTexturedModalRect(xPos + x, yPos + y, xInactivePos,
						yInactivePos, xSize, ySize);
			}
		}
	}
}
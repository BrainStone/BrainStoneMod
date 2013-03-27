package mods.brainstone.templates;

import mods.brainstone.guis.GuiBrainStoneTrigger;

public class BrainStoneButton
{
  public boolean inactive;
  public boolean down;
  public final int ID;
  public final int xSize;
  public final int ySize;
  public final int xPos;
  public final int yPos;
  public final int xDownPos;
  public final int yDownPos;
  public final int xInactivePos;
  public final int yInactivePos;
  public int triggerXmin;
  public int triggerYmin;
  public int triggerXmax;
  public int triggerYmax;

  public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos)
  {
    this(ID, xSize, ySize, xPos, yPos, 0, 0);
  }

  public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos, int xDownPos, int yDownPos)
  {
    this(ID, xSize, ySize, xPos, yPos, xDownPos, yDownPos, 0, 0);
  }

  public BrainStoneButton(int ID, int xSize, int ySize, int xPos, int yPos, int xDownPos, int yDownPos, int xInactivePos, int yInactivePos)
  {
    this.ID = ID;
    this.xSize = xSize;
    this.ySize = ySize;
    this.xPos = xPos;
    this.yPos = yPos;
    this.xDownPos = xDownPos;
    this.yDownPos = yDownPos;
    this.xInactivePos = xInactivePos;
    this.yInactivePos = yInactivePos;

    this.inactive = false;
    this.down = false;

    this.triggerXmin = this.xPos;
    this.triggerYmin = this.yPos;
    this.triggerXmax = (this.xPos + this.xSize);
    this.triggerYmax = (this.yPos + this.ySize);
  }

  public BrainStoneButton changeTriggerBox(int triggerXmin, int triggerYmin, int triggerXmax, int triggerYmax)
  {
    this.triggerXmin = triggerXmin;
    this.triggerYmin = triggerYmin;
    this.triggerXmax = triggerXmax;
    this.triggerYmax = triggerYmax;

    return this;
  }

  public void render(GuiBrainStoneTrigger gui, int x, int y) {
    if ((this.down) && (!this.inactive)) {
      gui.b(this.xPos + x, this.yPos + y, this.xDownPos, this.yDownPos, this.xSize, this.ySize);
    }
    else if (this.inactive)
      gui.b(this.xPos + x, this.yPos + y, this.xInactivePos, this.yInactivePos, this.xSize, this.ySize);
  }
}
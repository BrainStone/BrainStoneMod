package net.braintonemod.src;

import java.util.ArrayList;

public class BrainStoneGuiButton
{
  private GuiBrainStoneTrigger gui;
  private ArrayList buttons;
  private int length;

  public BrainStoneGuiButton(GuiBrainStoneTrigger Gui)
  {
    this.gui = Gui;
    this.buttons = new ArrayList();
    this.length = 0;
  }

  public void addButton(BrainStoneButton button)
  {
    this.buttons.add(button);
    this.length = this.buttons.size();
  }

  public void render(int x, int y)
  {
    for (int i = 0; i < this.length; i++)
    {
      ((BrainStoneButton)this.buttons.get(i)).render(this.gui, x, y);
    }
  }

  public void onClick(int x, int y)
  {
    for (int i = 0; i < this.length; i++)
    {
      BrainStoneButton tmp = (BrainStoneButton)this.buttons.get(i);

      if ((inField(x, y, tmp.triggerXmin, tmp.triggerYmin, tmp.triggerXmax, tmp.triggerYmax)) && (!tmp.inactive))
      {
        if (inField(x, y, tmp.xPos, tmp.yPos, tmp.xPos + tmp.xSize, tmp.yPos + tmp.ySize)) {
          tmp.down = true;
        }
        this.gui.buttonClicked(tmp.ID);
      }
      else
      {
        tmp.down = false;
      }
    }
  }

  public void hover()
  {
    for (int i = 0; i < this.length; i++)
    {
      ((BrainStoneButton)this.buttons.get(i)).down = false;
    }
  }

  public BrainStoneButton getButton(int ID)
  {
    for (int i = 0; i < this.length; i++)
    {
      BrainStoneButton tmp = (BrainStoneButton)this.buttons.get(i);

      if (tmp.ID == ID) {
        return tmp;
      }
    }
    return null;
  }

  private boolean inField(int i, int l, int i1, int j1, int k1, int l1)
  {
    return (i >= i1) && (i <= k1) && (l >= j1) && (l <= l1);
  }
}
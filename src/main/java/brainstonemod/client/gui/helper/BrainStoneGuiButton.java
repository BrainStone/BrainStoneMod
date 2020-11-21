package brainstonemod.client.gui.helper;

import brainstonemod.client.gui.GuiBrainStoneTrigger;
import java.util.ArrayList;

public class BrainStoneGuiButton {
  private final GuiBrainStoneTrigger gui;
  private final ArrayList<BrainStoneButton> buttons;
  private int length;

  public BrainStoneGuiButton(GuiBrainStoneTrigger Gui) {
    gui = Gui;
    buttons = new ArrayList<>();
    length = 0;
  }

  public void addButton(BrainStoneButton button) {
    buttons.add(button);
    length = buttons.size();
  }

  public BrainStoneButton getButton(int ID) {
    BrainStoneButton tmp;

    for (int i = 0; i < length; i++) {
      tmp = buttons.get(i);

      if (tmp.ID == ID) return tmp;
    }

    return null;
  }

  public void hover() {
    for (int i = 0; i < length; i++) {
      buttons.get(i).down = false;
    }
  }

  private boolean inField(int x, int y, int xMin, int yMin, int xMax, int yMax) {
    return (x >= xMin) && (x <= xMax) && (y >= yMin) && (y <= yMax);
  }

  public void onClick(int x, int y) {
    for (BrainStoneButton tmp : buttons) {
      if (inField(x, y, tmp.triggerXmin, tmp.triggerYmin, tmp.triggerXmax, tmp.triggerYmax)
          && (!tmp.inactive)) {
        tmp.down = true;

        gui.click();
        gui.buttonClicked(tmp.ID);
      } else {
        tmp.down = false;
      }
    }
  }

  public void onRelease() {
    for (BrainStoneButton tmp : buttons) {
      tmp.down = false;
    }
  }

  public void render(int x, int y) {
    for (int i = 0; i < length; i++) {
      buttons.get(i).render(gui, x, y);
    }
  }
}

package brainstonemod.templates;

import java.util.ArrayList;

import brainstonemod.guis.GuiBrainStoneTrigger;

public class BrainStoneGuiButton {
	private final GuiBrainStoneTrigger gui;
	private final ArrayList<BrainStoneButton> buttons;
	private int length;

	public BrainStoneGuiButton(GuiBrainStoneTrigger Gui) {
		gui = Gui;
		buttons = new ArrayList<BrainStoneButton>();
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

			if (tmp.ID == ID)
				return tmp;
		}

		return null;
	}

	public void hover() {
		for (int i = 0; i < length; i++) {
			buttons.get(i).down = false;
		}
	}

	private boolean inField(int i, int l, int i1, int j1, int k1, int l1) {
		return (i >= i1) && (i <= k1) && (l >= j1) && (l <= l1);
	}

	public void onClick(int x, int y) {
		BrainStoneButton tmp;

		for (int i = 0; i < length; i++) {
			tmp = buttons.get(i);

			if (this.inField(x, y, tmp.triggerXmin, tmp.triggerYmin,
					tmp.triggerXmax, tmp.triggerYmax) && (!tmp.inactive)) {
				if (this.inField(x, y, tmp.xPos, tmp.yPos,
						tmp.xPos + tmp.xSize, tmp.yPos + tmp.ySize)) {
					tmp.down = true;
				}

				gui.buttonClicked(tmp.ID);
			} else {
				tmp.down = false;
			}
		}
	}

	public void render(int x, int y) {
		for (int i = 0; i < length; i++) {
			buttons.get(i).render(gui, x, y);
		}
	}
}
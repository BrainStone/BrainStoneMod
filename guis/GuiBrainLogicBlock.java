package mods.brainstone.guis;

import mods.brainstone.BrainStone;
import mods.brainstone.containers.ContainerBlockBrainLightSensor;
import mods.brainstone.logicgates.Gate;
import mods.brainstone.templates.GuiBrainStoneBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiBrainLogicBlock extends GuiBrainStoneBase {
	private int globalX;
	private int globalY;
	private static final int xSizeMain = 176;
	private static final int ySizeMain = 200;
	private static final int xSizeHelp = 256;
	private float factor;
	private final TileEntityBlockBrainLogicBlock tileentity;
	private final String username;
	private boolean help;

	private final static int stringWidth = xSizeHelp - 20;
	private String HelpText;

	private int scrollbarPos;
	private static final int rowsToScroll = Gate.NumberGates - 6;
	private static final float pixelPerRow = 99.0F / rowsToScroll;

	public GuiBrainLogicBlock(
			TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock) {
		super(new ContainerBlockBrainLightSensor());
		username = BrainStone.proxy.getPlayer().username;
		tileentity = tileentityblockbrainlogicblock;
		tileentity.logIn(username);
		help = false;

		scrollbarPos = 0;
	}

	private void click() {
		mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	private void closeHelpGui() {
		this.click();
		help = false;
		tileentity.logIn(username);
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void drawFocus(int i, int j) {
		final int k = (int) (BrainStone.proxy.getClientWorld().getWorldInfo()
				.getWorldTime() & 1L) * 20;
		this.drawTexturedModalRect(i, j, 196, k, 20, 20);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float par1Float,
			int par2Integer, int par3Integer) {
		if (help) {
			this.registerTexture("GuiBrainLogicBlockhelp");

			final int rows = this.getLines(HelpText);
			final int ySizeHelp = 20 + (9 * rows);

			final int x = (width - xSizeHelp) / 2;
			final int y = (height - ySizeHelp) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSizeHelp, 10);
			this.drawTexturedModalRect(x, (y + ySizeHelp) - 10, 0, 19,
					xSizeHelp, 10);

			for (int row = 0; row < rows; row++) {
				this.drawTexturedModalRect(x, y + 10 + (row * 9), 0, 10,
						xSizeHelp, 9);
			}

			fontRenderer.drawSplitString(HelpText, x + 10, y + 10, stringWidth,
					0xeeeeee);
		} else {
			this.registerTexture();

			GL11.glPushMatrix();

			factor = 1.0F;
			final int x = globalX = (width - xSizeMain) / 2;
			final int y = globalY = (height - ySizeMain) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSizeMain, ySizeMain);

			if (rowsToScroll < 1) {
				this.drawTexturedModalRect(x + 157, y + 78, 244, 0, 12, 15);
			} else {
				this.drawTexturedModalRect(x + 157, y + 78
						+ ((int) (scrollbarPos * pixelPerRow)), 232, 0, 143, 15);
			}

			if (Gate.NumberGates < 6) {
				for (int i = 0; i < 6; i++) {
					if (i >= Gate.NumberGates) {
						this.drawTexturedModalRect(x + 8, y + 78 + (19 * i), 8,
								200, 12, 19);
					}
				}
			}

			final float factorConstant = 13.0F / 7.0F;

			for (int i = 0; i < 6; i++) {
				if (i < Gate.NumberGates) {
					this.drawString(Gate.GateNames[i + scrollbarPos], 11,
							81 + (19 * i), 0, factorConstant);
				}
			}

			scrollbarPos = 1 * scrollbarPos;

			GL11.glPopMatrix();
		}
	}

	public void drawSplitString(String s, int i, int j, int k, int l) {
		fontRenderer.drawSplitString(s, (int) (i / factor), (int) (j / factor),
				l, k);
	}

	public void drawString(String s, int i, int j, int k) {
		fontRenderer.drawString(s, (int) (i / factor), (int) (j / factor), k);
	}

	private void drawString(String s, int i, int j, int k, float f) {
		if (f != factor) {
			factor = f;
			GL11.glPopMatrix();
			GL11.glPushMatrix();

			// if (factor == 2.0F) {
			GL11.glTranslatef(globalX - 1.0F, globalY, 0.0F);
			// }

			GL11.glScalef(factor, factor, factor);
		}

		fontRenderer.drawString(s, (int) (i / factor), (int) (j / factor), k);
	}

	private int getLines(String str) {
		return fontRenderer.listFormattedStringToWidth(str, stringWidth).size();
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() {
		if (Mouse.getEventButton() == -1) {
			this.mouseMovedOrUp((Mouse.getEventX() * width) / mc.displayWidth,
					height - ((Mouse.getEventY() * height) / mc.displayHeight)
							- 1, Mouse.getEventButton());
		}

		super.handleMouseInput();
	}

	private boolean inField(int i, int j, int k, int l, int i1, int j1) {
		return (i >= k) && (i <= i1) && (j >= l) && (j <= j1);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char c, int i) {
		if (help) {
			this.closeHelpGui();
		} else {
			if ((i == Keyboard.KEY_ESCAPE)
					|| (i == mc.gameSettings.keyBindInventory.keyCode)) {
				this.quit();
			}

			// if (i == 205) {
			// this.swap(true);
			// }
			//
			// if (i == 203) {
			// this.swap(false);
			// }
			//
			// if (i == 54) {
			// this.rotate(true);
			// }
			//
			// if (i == 42) {
			// this.rotate(false);
			// }
			//
			// if ((i == 42) || (i == 54) || (i == 203) || (i == 205)) {
			// this.click();
			// }
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);

		if (help) {
			this.closeHelpGui();
		} else {
			i -= (width - xSizeMain) / 2;
			j -= (height - ySizeMain) / 2;

			if (this.inField(i, j, 168, 3, 172, 7)) {
				this.quit();

				return;
			}

			// for (byte byte0 = 0; byte0 <
			// TileEntityBlockBrainLogicBlock.numGates; byte0++) {
			// final int l = 12 * byte0;
			//
			// if (this.inField(i, j, 5, 18 + l, 75, 32 + l)) {
			// tileentity.setMode(byte0);
			// }
			// }
			//
			// if (this.inField(i, j, 76, 68, 168, 90)) {
			// tileentity.invertInvertOutput();
			// }
			//
			// if (!tileentity.isSwapable()) {
			// tileentity.setFocused(0);
			// } else if (this.inField(i, j, 124, 7, 143, 26)) {
			// focused = tileentity.getFocused();
			//
			// if (focused != 1) {
			// tileentity.setFocused(1);
			// } else {
			// tileentity.setFocused(0);
			// }
			// } else if (this.inField(i, j, 144, 27, 163, 46)) {
			// if (focused != 2) {
			// tileentity.setFocused(2);
			// } else {
			// tileentity.setFocused(0);
			// }
			// } else if (this.inField(i, j, 104, 27, 123, 46)) {
			// if (focused != 3) {
			// tileentity.setFocused(3);
			// } else {
			// tileentity.setFocused(0);
			// }
			// } else {
			// tileentity.setFocused(0);
			// }
		}
	}

	private void quit() {
		this.click();
		tileentity.logOut(username);
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}

	// private void rotate(boolean flag) {
	// if (flag) {
	// tileentity.swapPosition(1, 2);
	// tileentity.swapPosition(1, 3);
	// tileentity.addTASKS("setFocused", new String[] { "0" });
	// } else {
	// tileentity.swapPosition(1, 3);
	// tileentity.swapPosition(1, 2);
	// tileentity.addTASKS("setFocused", new String[] { "0" });
	// }
	// }
	//
	// private void swap(boolean flag) {
	// focused = tileentity.getFocused();
	//
	// if (flag) {
	// switch (focused) {
	// case 1:
	// tileentity.swapPosition(1, 2);
	// break;
	//
	// case 2:
	// tileentity.swapPosition(2, 3);
	// break;
	//
	// case 3:
	// tileentity.swapPosition(3, 1);
	// break;
	// }
	// } else {
	// switch (focused) {
	// case 1:
	// tileentity.swapPosition(1, 3);
	// break;
	//
	// case 2:
	// tileentity.swapPosition(2, 1);
	// break;
	//
	// case 3:
	// tileentity.swapPosition(3, 2);
	// break;
	// }
	// }
	// }
}
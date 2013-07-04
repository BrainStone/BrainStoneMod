package mods.brainstonemod.guis;

import mods.brainstonemod.BrainStone;
import mods.brainstonemod.containers.ContainerBlockBrainLightSensor;
import mods.brainstonemod.templates.GuiBrainStoneBase;
import mods.brainstonemod.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiBrainLogicBlock extends GuiBrainStoneBase {
	private byte focused;
	private int globalX;
	private int globalY;
	private static final int xSize = 176;
	private static final int ySize = 166;
	private float factor;
	private final TileEntityBlockBrainLogicBlock tileentity;
	private final String username;
	private boolean help;

	private final static int helpYSize = 256 - ySize;
	private final static int stringWidth = xSize - 20;
	private String HelpText;

	public GuiBrainLogicBlock(
			TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock) {
		super(new ContainerBlockBrainLightSensor());
		username = BrainStone.proxy.getPlayer().username;
		tileentity = tileentityblockbrainlogicblock;
		tileentity.logIn(username);
		focused = tileentity.getFocused();
		help = false;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			HelpText = StatCollector.translateToLocal((new StringBuilder())
					.append("gui.brainstone.help.gate")
					.append(String.valueOf(tileentity.getMode())).toString());

			help = true;
			buttonList.clear();
			tileentity.logOut(username);
		}
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
	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		this.registerTexture();

		if (help) {
			final int l = (width - xSize) / 2;
			final int i1 = (height - helpYSize) / 2;
			this.drawTexturedModalRect(l, i1, 0, ySize, xSize, helpYSize);
			fontRenderer.drawSplitString(HelpText, l + 10, i1 + 10,
					stringWidth, 0xeeeeee);
		} else {
			factor = 1.0F;
			final int l = globalX = (width - xSize) / 2;
			final int i1 = globalY = (height - ySize) / 2;
			this.drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
			tileentity.drawBoxes(this, l + 104, i1 + 7);
			focused = tileentity.getFocused();

			if (focused != 0) {
				switch (focused) {
				case 1:
					this.drawFocus(l + 124, i1 + 7);
					break;

				case 2:
					this.drawFocus(l + 144, i1 + 27);
					break;

				case 3:
					this.drawFocus(l + 104, i1 + 27);
					break;
				}
			}

			GL11.glPushMatrix();
			GL11.glScalef(factor, factor, factor);
			tileentity.drawGates(this, l + 6, i1 + 20);
			this.drawString(
					StatCollector.translateToLocal("tile.brainLogicBlock.name"),
					l + 6, i1 + 6, 0);
			final boolean aflag[] = tileentity.shallRender();

			if (aflag[0]) {
				this.drawString(tileentity.getPin(0), 130, 50,
						tileentity.getPinColor(0), 2.0F);
			}

			if (aflag[1]) {
				this.drawString(tileentity.getPin(1), 130, 10,
						tileentity.getPinColor(1), 2.0F);
			}

			if (aflag[2]) {
				this.drawString(tileentity.getPin(2), 150, 30,
						tileentity.getPinColor(2), 2.0F);
			}

			if (aflag[3]) {
				this.drawString(tileentity.getPin(3), 110, 30,
						tileentity.getPinColor(3), 2.0F);
			}

			GL11.glPopMatrix();
			this.initGui();
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

			if (factor == 2.0F) {
				GL11.glTranslatef(globalX - 1.0F, globalY, 0.0F);
			}

			GL11.glScalef(factor, factor, factor);
		}

		fontRenderer.drawString(s, (int) (i / factor), (int) (j / factor), k);
	}

	private boolean inField(int i, int j, int k, int l, int i1, int j1) {
		return (i >= k) && (i <= i1) && (j >= l) && (j <= j1);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, globalX + 10, globalY + 140, 156, 20,
				StatCollector.translateToLocal("gui.brainstone.help")));
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
			if ((i == 1) || (i == mc.gameSettings.keyBindInventory.keyCode)) {
				this.quit();
			}

			if (i == 205) {
				this.swap(true);
			}

			if (i == 203) {
				this.swap(false);
			}

			if (i == 54) {
				this.rotate(true);
			}

			if (i == 42) {
				this.rotate(false);
			}

			if ((i == 42) || (i == 54) || (i == 203) || (i == 205)) {
				this.click();
			}
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int i, int j, int k) {
		final boolean flag = help;

		super.mouseClicked(i, j, k);

		if ((k != 0) || (flag != help))
			return;

		if (help) {
			this.closeHelpGui();
		} else {
			i -= (width - xSize) / 2;
			j -= (height - ySize) / 2;

			if (this.inField(i, j, 168, 3, 172, 7)) {
				this.quit();

				return;
			}

			for (byte byte0 = 0; byte0 < TileEntityBlockBrainLogicBlock.numGates; byte0++) {
				final int l = 12 * byte0;

				if (this.inField(i, j, 5, 18 + l, 75, 32 + l)) {
					tileentity.setMode(byte0);
				}
			}

			if (this.inField(i, j, 76, 68, 168, 90)) {
				tileentity.invertInvertOutput();
			}

			if (!tileentity.isSwapable()) {
				tileentity.setFocused(0);
			} else if (this.inField(i, j, 124, 7, 143, 26)) {
				focused = tileentity.getFocused();

				if (focused != 1) {
					tileentity.setFocused(1);
				} else {
					tileentity.setFocused(0);
				}
			} else if (this.inField(i, j, 144, 27, 163, 46)) {
				if (focused != 2) {
					tileentity.setFocused(2);
				} else {
					tileentity.setFocused(0);
				}
			} else if (this.inField(i, j, 104, 27, 123, 46)) {
				if (focused != 3) {
					tileentity.setFocused(3);
				} else {
					tileentity.setFocused(0);
				}
			} else {
				tileentity.setFocused(0);
			}
		}
	}

	private void quit() {
		this.click();
		tileentity.logOut(username);
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}

	private void rotate(boolean flag) {
		if (flag) {
			tileentity.swapPosition(1, 2);
			tileentity.swapPosition(1, 3);
			tileentity.addTASKS("setFocused", new String[] { "0" });
		} else {
			tileentity.swapPosition(1, 3);
			tileentity.swapPosition(1, 2);
			tileentity.addTASKS("setFocused", new String[] { "0" });
		}
	}

	private void swap(boolean flag) {
		focused = tileentity.getFocused();

		if (flag) {
			switch (focused) {
			case 1:
				tileentity.swapPosition(1, 2);
				break;

			case 2:
				tileentity.swapPosition(2, 3);
				break;

			case 3:
				tileentity.swapPosition(3, 1);
				break;
			}
		} else {
			switch (focused) {
			case 1:
				tileentity.swapPosition(1, 3);
				break;

			case 2:
				tileentity.swapPosition(2, 1);
				break;

			case 3:
				tileentity.swapPosition(3, 2);
				break;
			}
		}
	}
}

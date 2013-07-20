package brainstonemod.guis;

import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;
import brainstonemod.containers.ContainerBlockBrainLightSensor;
import brainstonemod.logicgates.Gate;
import brainstonemod.templates.GuiBrainStoneBase;
import brainstonemod.tileentities.TileEntityBlockBrainLogicBlock;

public class GuiBrainLogicBlock extends GuiBrainStoneBase {
	private static boolean isFormatColor(char par0) {
		return ((par0 >= 48) && (par0 <= 57))
				|| ((par0 >= 97) && (par0 <= 102))
				|| ((par0 >= 65) && (par0 <= 70));
	}

	private int globalX;
	private int globalY;
	private static final int xSizeMain = 176;
	private static final int ySizeMain = 200;
	private static final int xSizeHelp = 256;
	private float factor;
	private final TileEntityBlockBrainLogicBlock tileentity;
	private final String username;
	private boolean help;

	private final byte direction;
	private final static int stringWidth = xSizeHelp - 20;

	private String HelpText;
	private int scrollbarPos;
	private byte mousePos;
	private int movingPin;
	private int movingPinOffsetX, movingPinOffsetY;
	private final byte swappedPin;

	private int mousePosX, mousePosY;
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
		mousePos = 1;
		movingPin = -1;
		movingPinOffsetX = 0;
		movingPinOffsetY = 0;
		swappedPin = -1;

		mousePosX = 0;
		mousePosY = 0;

		direction = TileEntityBlockBrainLogicBlock.guiDirection;
	}

	private void click() {
		mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	private void closeHelpGui() {
		this.click();
		help = false;
		tileentity.logIn(username);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float par1Float,
			int par2Integer, int par3Integer) {
		if (mousePos == -2) {
			this.mouseMovedOrUp((Mouse.getEventX() * width) / mc.displayWidth,
					height - ((Mouse.getEventY() * height) / mc.displayHeight)
							- 1, -1);
		}

		this.registerTexture();

		GL11.glPushMatrix();

		factor = 1.0F;
		int x = globalX = (width - xSizeMain) / 2;
		int y = globalY = (height - ySizeMain) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSizeMain, ySizeMain);

		if (rowsToScroll < 1) {
			this.drawTexturedModalRect(x + 157, y + 78, 244, 0, 12, 15);
		} else {
			this.drawTexturedModalRect(x + 157, y + 78
					+ ((int) (scrollbarPos * pixelPerRow)), 232, 0, 12, 15);
		}

		for (int i = 0; i < 6; i++) {
			if ((Gate.NumberGates >= 6) || (i < Gate.NumberGates)) {
				this.drawTexturedModalRect(x + 8, y + 78 + (19 * i), 8,
						((i + scrollbarPos) == tileentity.getGatePos()) ? 219
								: 200, 143, 19, i == mousePos);
			}
		}

		if (movingPin != 0) {
			this.renderGateFrameAt(x + 38, y + 7, (byte) 0);
		} else if (swappedPin != -1) {
			this.renderGateFrameAt(x + 38, y + 7, swappedPin);
		}

		if (movingPin != 1) {
			this.renderGateFrameAt(x + 38, y + 47, (byte) 1);
		} else if (swappedPin != -1) {
			this.renderGateFrameAt(x + 38, y + 47, swappedPin);
		}

		if (movingPin != 2) {
			this.renderGateFrameAt(x + 98, y + 7, (byte) (2 + direction));
		} else if (swappedPin != -1) {
			this.renderGateFrameAt(x + 98, y + 7, swappedPin);
		}

		if (movingPin != 3) {
			this.renderGateFrameAt(x + 78, y + 27,
					(byte) (2 + ((direction + 3) & 3)));
		} else if (swappedPin != -1) {
			this.renderGateFrameAt(x + 78, y + 27, swappedPin);
		}

		if (movingPin != 4) {
			this.renderGateFrameAt(x + 98, y + 47,
					(byte) (2 + ((direction + 2) & 3)));
		} else if (swappedPin != -1) {
			this.renderGateFrameAt(x + 98, y + 47, swappedPin);
		}

		if (movingPin != 5) {
			this.renderGateFrameAt(x + 118, y + 27,
					(byte) (2 + ((direction + 1) & 3)));
		} else if (swappedPin != -1) {
			this.renderGateFrameAt(x + 118, y + 27, swappedPin);
		}

		// END of Textures!
		// BEGIN of Strings!

		GL11.glTranslatef(globalX, globalY, 0.0F);

		for (int i = 0; i < 6; i++) {
			if (i < Gate.NumberGates) {
				this.drawString(Gate.GateNames[i + scrollbarPos], 14,
						84 + (19 * i), 0);
			}
		}

		final String directions[] = new String[] { "North", "East", "South",
				"West" };

		this.drawString("Top", 35 - this.getStringWidth("Top"), 13, 0);
		this.drawString("Bottom", 61, 53, 0);
		this.drawString(directions[direction],
				95 - this.getStringWidth(directions[direction]), 13, 0);
		this.drawString(directions[direction ^ 2], 121, 53, 0);

		if (movingPin != 0) {
			this.renderGateLetterAt(42, 11, (byte) 0);
		} else if (swappedPin != -1) {
			this.renderGateLetterAt(42, 11, swappedPin);
		}

		if (movingPin != 1) {
			this.renderGateLetterAt(42, 51, (byte) 1);
		} else if (swappedPin != -1) {
			this.renderGateLetterAt(42, 51, swappedPin);
		}

		if (movingPin != 2) {
			this.renderGateLetterAt(102, 11, (byte) (2 + direction));
		} else if (swappedPin != -1) {
			this.renderGateLetterAt(102, 11, swappedPin);
		}

		if (movingPin != 3) {
			this.renderGateLetterAt(82, 31, (byte) (2 + ((direction + 3) & 3)));
		} else if (swappedPin != -1) {
			this.renderGateLetterAt(82, 31, swappedPin);
		}

		if (movingPin != 4) {
			this.renderGateLetterAt(102, 51, (byte) (2 + ((direction + 2) & 3)));
		} else if (swappedPin != -1) {
			this.renderGateLetterAt(102, 51, swappedPin);
		}

		if (movingPin != 5) {
			this.renderGateLetterAt(122, 31, (byte) (2 + ((direction + 1) & 3)));
		} else if (swappedPin != -1) {
			this.renderGateLetterAt(122, 31, swappedPin);
		}

		scrollbarPos = 1 * scrollbarPos;

		GL11.glPopMatrix();

		// MovingPin

		if (movingPin != -1) {
			GL11.glPushMatrix();
			factor = 1.0F;
			this.registerTexture();

			byte pos = -1;

			switch (movingPin) {
			case 0:
				pos = 0;
				break;
			case 1:
				pos = 1;
				break;
			case 2:
				pos = (byte) (2 + direction);
				break;
			case 3:
				pos = (byte) (2 + ((direction + 3) & 3));
				break;
			case 4:
				pos = (byte) (2 + ((direction + 2) & 3));
				break;
			case 5:
				pos = (byte) (2 + ((direction + 1) & 3));
				break;
			}

			this.renderGateFrameAt((x + mousePosX) - movingPinOffsetX,
					(y + mousePosY) - movingPinOffsetY, pos);

			GL11.glTranslatef(globalX, globalY, 0.0F);

			this.renderGateLetterAt((mousePosX - movingPinOffsetX) + 4,
					(mousePosY - movingPinOffsetY) + 4, pos);

			GL11.glPopMatrix();
		}

		// Help Screen!

		if (help) {
			this.registerTexture("GuiBrainLogicBlockhelp");

			final int rows = this.getLines(HelpText);
			final int ySizeHelp = 20 + (9 * rows);

			x = (width - xSizeHelp) / 2;
			y = (height - ySizeHelp) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSizeHelp, 10);
			this.drawTexturedModalRect(x, (y + ySizeHelp) - 10, 0, 19,
					xSizeHelp, 10);

			for (int row = 0; row < rows; row++) {
				this.drawTexturedModalRect(x, y + 10 + (row * 9), 0, 10,
						xSizeHelp, 9);
			}

			fontRenderer.drawSplitString(HelpText, x + 10, y + 10, stringWidth,
					0xeeeeee);
		}
	}

	private void drawSplitString(String s, int i, int j, int k, int l) {
		fontRenderer.drawSplitString(s, (int) (i / factor), (int) (j / factor),
				l, k);
	}

	private void drawString(String s, int i, int j, int k) {
		fontRenderer.drawString(s, (int) (i / factor), (int) (j / factor), k);
	}

	private void drawString(String s, int i, int j, int k, float f) {
		if (f != factor) {
			factor = f;
			GL11.glPopMatrix();
			GL11.glPushMatrix();

			GL11.glTranslatef(globalX + 1, globalY, 0.0F);

			GL11.glScalef(factor, factor, factor);
		}

		fontRenderer.drawString(s, (int) (i / factor), (int) (j / factor), k);
	}

	private void drawTexturedModalRect(int x, int y, int u, int v, int width,
			int height, boolean inverted) {
		if (inverted) {
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

			this.drawTexturedModalRect(-x - width, -y - height, u, v, width,
					height);

			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		} else {
			this.drawTexturedModalRect(x, y, u, v, width, height);
		}
	}

	private int getLines(String str) {
		return fontRenderer.listFormattedStringToWidth(str, stringWidth).size();
	}

	private int getStringWidth(String str) {
		final int j = str.length();
		int k = 0;
		int l = 0;

		for (boolean flag = false; l < j; ++l) {
			final char c0 = str.charAt(l);

			switch (c0) {
			case 10:
				--l;
				break;
			case 167:
				if (l < (j - 1)) {
					++l;
					final char c1 = str.charAt(l);

					if ((c1 != 108) && (c1 != 76)) {
						if ((c1 == 114) || (c1 == 82) || isFormatColor(c1)) {
							flag = false;
						}
					} else {
						flag = true;
					}
				}

				break;
			default:
				k += fontRenderer.getCharWidth(c0);

				if (flag) {
					++k;
				}
			}

			if (c0 == 10) {
				++l;
				break;
			}
		}

		return k;
	}

	@Override
	public void handleMouseInput() {
		if (Mouse.getEventButton() == -1) {
			this.mouseMovedOrUp((Mouse.getEventX() * width) / mc.displayWidth,
					height - ((Mouse.getEventY() * height) / mc.displayHeight)
							- 1, -1);
		}

		super.handleMouseInput();
	}

	private boolean inField(int i, int j, int k, int l, int i1, int j1) {
		return (i >= k) && (i <= i1) && (j >= l) && (j <= j1);
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (help) {
			this.closeHelpGui();
		} else {
			if ((i == Keyboard.KEY_ESCAPE)
					|| (i == mc.gameSettings.keyBindInventory.keyCode)) {
				this.quit();
			} else if (i == Keyboard.KEY_F1) {
				this.openHelp();
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

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int which) {
		super.mouseClicked(mouseX, mouseY, which);

		if (help) {
			this.closeHelpGui();
		} else {
			mousePosX = mouseX -= globalX;
			mousePosY = mouseY -= globalY;

			for (int i = 0; i < 6; i++) {
				if (this.inField(mouseX, mouseY, 8, 78 + (19 * i), 150,
						96 + (19 * i))
						&& ((i + scrollbarPos) < Gate.NumberGates)) {
					tileentity.changeGate(Gate.GateNames[i + scrollbarPos],
							direction);

					break;
				}
			}

			// TODO check if pin is movable and exists
			if (this.inField(mouseX, mouseY, 38, 7, 58, 27)) {
				movingPin = 0;
				movingPinOffsetX = mouseX - 38;
				movingPinOffsetY = mouseY - 7;
			} else if (this.inField(mouseX, mouseY, 38, 47, 58, 67)) {
				movingPin = 1;
				movingPinOffsetX = mouseX - 38;
				movingPinOffsetY = mouseY - 47;
			} else if (this.inField(mouseX, mouseY, 98, 7, 118, 27)) {
				movingPin = 2;
				movingPinOffsetX = mouseX - 98;
				movingPinOffsetY = mouseY - 7;
			} else if (this.inField(mouseX, mouseY, 78, 27, 98, 47)) {
				movingPin = 3;
				movingPinOffsetX = mouseX - 78;
				movingPinOffsetY = mouseY - 27;
			} else if (this.inField(mouseX, mouseY, 98, 47, 118, 67)) {
				movingPin = 4;
				movingPinOffsetX = mouseX - 98;
				movingPinOffsetY = mouseY - 47;
			} else if (this.inField(mouseX, mouseY, 118, 27, 138, 47)) {
				movingPin = 5;
				movingPinOffsetX = mouseX - 118;
				movingPinOffsetY = mouseY - 27;
			}

			if (this.inField(mouseX, mouseY, 168, 3, 172, 7)) {
				this.quit();

				return;
			}

			// REMOVE
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

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
		if (!help) {
			mousePosX = mouseX -= globalX;
			mousePosY = mouseY -= globalY;

			if (which == -1) {
				mousePos = -1;
				for (int i = 0; i < 6; i++) {
					if (this.inField(mouseX, mouseY, 8, 78 + (19 * i), 150,
							96 + (19 * i))) {
						mousePos = (byte) i;

						break;
					}
				}
			} else {
				if (movingPin != -1) {
					movingPin = -1;
				}
			}
		}
	}

	private void openHelp() {
		// TODO Determine what text to open depending on the mouse position
		final String topic = "Trolololo";

		if (!topic.isEmpty()) {
			String translatedTitle = StatCollector.translateToLocal(topic
					+ ".name");
			final int spacesToAdd = (stringWidth - fontRenderer
					.getStringWidth(translatedTitle)) / 8;

			for (int i = 0; i < spacesToAdd; i++) {
				translatedTitle = " " + translatedTitle;
			}

			help = true;
			HelpText = translatedTitle
					+ "\n=======================================\n"
					+ StatCollector.translateToLocal(topic + ".desc");
		}
	}

	private void quit() {
		this.click();
		tileentity.logOut(username);
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}

	private void renderGateFrameAt(int x, int y, byte pos) {
		if (tileentity.shallRenderPin(pos)) {
			final byte powerLevel = tileentity.getPowerLevel(pos);
			if (powerLevel == -1) {
				this.drawTexturedModalRect(x, y, 216, 20, 20, 20);
			} else {
				this.drawTexturedModalRect(x, y,
						176 + (((powerLevel & 8) >> 1) * 5),
						(powerLevel & 7) * 20, 20, 20);
			}
		}
	}

	private void renderGateLetterAt(int x, int y, byte pos) {
		if (tileentity.shallRenderPin(pos)) {
			this.drawString(tileentity.getGateLetter(pos), x, y,
					tileentity.getGateColor(pos), 2.0F);
		}
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
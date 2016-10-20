package brainstonemod.client.gui;

import brainstonemod.BrainStone;
import brainstonemod.client.gui.template.GuiBrainStoneBase;
import brainstonemod.common.container.ContainerGeneric;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDirection;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.tileentity.TileEntityBrainLogicBlock;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.serverbound.PacketChangeGate;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.Random;
import java.util.UUID;

import static brainstonemod.common.helper.BrainStoneDirection.*;

public class GuiBrainLogicBlock extends GuiBrainStoneBase {
	private class SoundLoop extends Thread {
		private boolean run;
		private ISound currentSound;

		public SoundLoop() {
			run = true;

			setPriority(MAX_PRIORITY);
		}

		@Override
		public void run() {
			try {
				currentSound = playSoundAtClient(BrainStone.RESOURCE_PREFIX
						+ "nyan.intro");
				sleep(4037);

				if (run) {
					renderNyanCat = true;
					lastAnimationProgress = System.currentTimeMillis()
							/ millisPerCatFrame;
				}

				while (run) {
					currentSound = playSoundAtClient(BrainStone.RESOURCE_PREFIX
							+ "nyan.loop");
					sleep(27066);
				}
			} catch (final InterruptedException e) {
				BSP.warnException(e);
			}
		}

		public void stopSound() {
			run = false;

			if (currentSound != null) {
				stopSoundAtClient(currentSound);
			}
		}
	}

	private class StarAnimation {
		private int x;
		private final int y;
		private int frame;

		public StarAnimation() {
			x = width / (catScale * 2);
			y = random.nextInt(height / catScale) - (height / (catScale * 2));

			frame = random.nextInt(6);
		}

		public void ranomizeX() {
			x = random.nextInt(width / catScale) - (width / (catScale * 2));
		}

		public void render() {
			drawTexturedModalRect(x - 3, y - 3, 34, frame * 7, 7, 7);
		}

		public boolean shift() {
			frame = (frame + 1) % 6;
			x -= 10;

			return x > (width / (catScale * -2));
		}
	}

	private static final int xSizeMain = 176;
	private static final int ySizeMain = 200;

	private static final int xSizeHelp = 256;

	private final static int stringWidth = xSizeHelp - 20;
	private static final int rowsToScroll = Gate.NumberGates - 6;
	private static final float pixelPerRow = 99.0F / rowsToScroll;

	private static final int catScale = 6;
	private static final int starEachPixels = 1000 * (catScale * catScale);
	private static final long millisPerCatFrame = 100L;

	private static boolean isFormatColor(char par0) {
		return ((par0 >= 48) && (par0 <= 57))
				|| ((par0 >= 97) && (par0 <= 102))
				|| ((par0 >= 65) && (par0 <= 70));
	}

	private final Random random = new Random();
	private final TileEntityBrainLogicBlock tileentity;
	private final UUID username;
	private boolean help;
	private final BrainStoneDirection direction;
	private String HelpText;
	private int scrollbarPos;
	private byte mousePos;
	private BrainStoneDirection movingPin;
	private int movingPinOffsetX, movingPinOffsetY;
	private final BrainStoneDirection swappedPin;
	private int mousePosX, mousePosY;
	private final BrainStoneDirection[] localToBlockDirections;
	private final char[] lastChars;
	private SoundLoop soundLoop;
	private boolean nyanCat;
	private boolean renderNyanCat;
	private long lastAnimationProgress;
	private int numberStars;
	private StarAnimation[] stars;

	public GuiBrainLogicBlock(TileEntityBrainLogicBlock te, EntityPlayer player) {
		super(new ContainerGeneric(), te);
		username = player.getUniqueID();
		tileentity = te;
		tileentity.logIn(username);
		help = false;

		scrollbarPos = 0;
		mousePos = 1;
		movingPin = null;
		movingPinOffsetX = 0;
		movingPinOffsetY = 0;
		swappedPin = null;

		mousePosX = 0;
		mousePosY = 0;

		direction = TileEntityBrainLogicBlock.guiDirection;

		localToBlockDirections = new BrainStoneDirection[6];

		localToBlockDirections[0] = UP;
		localToBlockDirections[1] = DOWN;

		localToBlockDirections[NORTH.toArrayIndex()] = NORTH
				.reorintateNorth(direction);
		localToBlockDirections[EAST.toArrayIndex()] = EAST
				.reorintateNorth(direction);
		localToBlockDirections[SOUTH.toArrayIndex()] = SOUTH
				.reorintateNorth(direction);
		localToBlockDirections[WEST.toArrayIndex()] = WEST
				.reorintateNorth(direction);

		lastChars = new char[4];
		soundLoop = null;
		nyanCat = false;

		setSize(xSizeMain, ySizeMain);
	}

	private void closeHelpGui() {
		click();
		help = false;
		tileentity.logIn(username);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void drawGuiBackground(float partialTicks, int mouseX, int mouseY) {
		if (mousePos == -2) {
			mouseMovedOrUp((Mouse.getEventX() * width) / mc.displayWidth,
					height - ((Mouse.getEventY() * height) / mc.displayHeight)
							- 1, -1);
		}

		drawTexturedModalRect(0, 0, 0, 0, xSizeMain, ySizeMain);

		if (rowsToScroll < 1) {
			drawTexturedModalRect(157, 78, 244, 0, 12, 15);
		} else {
			drawTexturedModalRect(157,
					78 + ((int) (scrollbarPos * pixelPerRow)), 232, 0, 12, 15);
		}

		for (int i = 0; i < 6; i++) {
			if ((Gate.NumberGates >= 6) || (i < Gate.NumberGates)) {
				drawTexturedModalRect(8, 78 + (19 * i), 8,
						((i + scrollbarPos) == tileentity.getGatePos()) ? 219
								: 200, 143, 19, i == mousePos);
			}
		}

		if (movingPin != UP) {
			renderGateFrameAt(38, 7, localToBlockDirections[UP.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateFrameAt(38, 7,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != DOWN) {
			renderGateFrameAt(38, 47,
					localToBlockDirections[DOWN.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateFrameAt(38, 47,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != NORTH) {
			renderGateFrameAt(98, 7,
					localToBlockDirections[NORTH.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateFrameAt(98, 7,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != EAST) {
			renderGateFrameAt(118, 27,
					localToBlockDirections[EAST.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateFrameAt(118, 27,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != SOUTH) {
			renderGateFrameAt(98, 47,
					localToBlockDirections[SOUTH.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateFrameAt(98, 47,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != WEST) {
			renderGateFrameAt(78, 27,
					localToBlockDirections[WEST.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateFrameAt(78, 27,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		// END of Textures!
		// BEGIN of Strings!

		for (int i = 0; i < 6; i++) {
			if (i < Gate.NumberGates) {
				drawString(Gate.GateNames[i + scrollbarPos], 14, 84 + (19 * i),
						0);
			}
		}

		final String topDirection = StatCollector
				.translateToLocal("gui.brainstone.top");
		final String bottomDirection = StatCollector
				.translateToLocal("gui.brainstone.bottom");
		final String forwardDirection = StatCollector
				.translateToLocal("gui.brainstone."
						+ direction.toString().toLowerCase());
		final String backwardDirection = StatCollector
				.translateToLocal("gui.brainstone."
						+ direction.getOpposite().toString().toLowerCase());

		drawString(topDirection, 35 - getStringWidth(topDirection), 13, 0);
		drawString(bottomDirection, 61, 53, 0);
		drawString(forwardDirection, 95 - getStringWidth(forwardDirection), 13,
				0);
		drawString(backwardDirection, 121, 53, 0);

		if (movingPin != UP) {
			renderGateLetterAt(43, 10,
					localToBlockDirections[UP.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateLetterAt(43, 10,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != DOWN) {
			renderGateLetterAt(43, 50,
					localToBlockDirections[DOWN.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateLetterAt(43, 50,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != NORTH) {
			renderGateLetterAt(103, 10,
					localToBlockDirections[NORTH.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateLetterAt(103, 10,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != EAST) {
			renderGateLetterAt(123, 30,
					localToBlockDirections[EAST.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateLetterAt(123, 30,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != SOUTH) {
			renderGateLetterAt(103, 50,
					localToBlockDirections[SOUTH.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateLetterAt(103, 50,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		if (movingPin != WEST) {
			renderGateLetterAt(83, 30,
					localToBlockDirections[WEST.toArrayIndex()]);
		} else if (swappedPin != null) {
			renderGateLetterAt(83, 30,
					localToBlockDirections[swappedPin.toArrayIndex()]);
		}

		scrollbarPos = 1 * scrollbarPos;

		// MovingPin

		if (movingPin != null) {
			bindTexture();

			renderGateFrameAt(mousePosX - movingPinOffsetX, mousePosY
					- movingPinOffsetY,
					localToBlockDirections[movingPin.toArrayIndex()]);

			renderGateLetterAt((mousePosX - movingPinOffsetX) + 5,
					(mousePosY - movingPinOffsetY) + 3,
					localToBlockDirections[movingPin.toArrayIndex()]);
		}

		// Help Screen!

		if (help) {
			drawMyDefaultBackground();

			bindTexture("GuiBrainLogicBlockHelp");

			final int rows = getLines(HelpText);
			final int ySizeHelp = 20 + (9 * rows);
			setTempSize(xSizeHelp, ySizeHelp);

			drawTexturedModalRect(0, 0, 0, 0, xSizeHelp, 10);
			drawTexturedModalRect(0, ySizeHelp - 10, 0, 19, xSizeHelp, 10);

			for (int row = 0; row < rows; row++) {
				drawTexturedModalRect(0, 10 + (row * 9), 0, 10, xSizeHelp, 9);
			}

			fontRendererObj.drawSplitString(HelpText, 10, 10, stringWidth,
					0xeeeeee);
		}

		if (renderNyanCat) {
			// Transforming the rendering space
			bindTexture("GuiBrainLogicBlockEasterEgg");
			setTempSize(0, 0);
			GL11.glScalef(catScale, catScale, catScale);

			// Checking for screen size change
			if (numberStars != ((width * height) / starEachPixels)) {
				numberStars = (width * height) / starEachPixels;
				stars = null;
			}

			// Refilling the stars array
			if (stars == null) {
				StarAnimation star;

				stars = new StarAnimation[numberStars];

				for (int i = 0; i < numberStars; i++) {
					star = new StarAnimation();
					star.ranomizeX();

					stars[i] = star;
				}
			}

			// Render the rainbow
			drawTexturedModalRect(-23, -9, 41,
					((int) ((lastAnimationProgress / 2) % 2L)) * 19, 16, 18);
			drawTexturedModalRect(-23, -9, 41,
					((int) ((lastAnimationProgress / 2) % 2L)) * 19, 14, 19);

			for (int i = 1; i < (((width / (catScale * 2)) / 16) + 1); i++) {
				drawTexturedModalRect((i * -16) - 23, -9, 41,
						((int) ((lastAnimationProgress / 2) % 2L)) * 19, 16, 19);
			}

			// Render the cat
			drawTexturedModalRect(-17, -10, 0,
					((int) (lastAnimationProgress % 12L)) * 21, 34, 21);

			for (final StarAnimation star : stars) {
				star.render();
			}

			// Progress the animation
			if ((System.currentTimeMillis() / millisPerCatFrame) > lastAnimationProgress) {
				lastAnimationProgress = System.currentTimeMillis()
						/ millisPerCatFrame;

				for (int i = 0; i < stars.length; i++) {
					if (!stars[i].shift()) {
						stars[i] = new StarAnimation();
					}
				}
			}
		}
	}

	private int getLines(String str) {
		return fontRendererObj.listFormattedStringToWidth(str, stringWidth)
				.size();
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
				k += fontRendererObj.getCharWidth(c0);

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
			mouseMovedOrUp((Mouse.getEventX() * width) / mc.displayWidth,
					height - ((Mouse.getEventY() * height) / mc.displayHeight)
							- 1, -1);
		}

		super.handleMouseInput();
	}

	@Override
	protected void keyTyped(char letter, int key) {
		if (help) {
			closeHelpGui();
		} else {
			if ((key == Keyboard.KEY_ESCAPE) || (key == mc.gameSettings.keyBindInventory.getKeyCode())) {
				quit();
			} else if (key == Keyboard.KEY_F1) {
				openHelp();
			}
		}

		if (nyanCat) {
			stopEasterEgg();
		}

		// Shift array by one
		System.arraycopy(lastChars, 0, lastChars, 1, lastChars.length - 1);
		lastChars[0] = letter;

		if (((lastChars[2] == 'l') && (lastChars[1] == 'o') && (lastChars[0] == 'l'))
				|| ((lastChars[3] == 'a') && (lastChars[2] == 's')
						&& (lastChars[1] == 'd') && (lastChars[0] == 'f'))) {
			startEasterEgg();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int which) {
		if (help) {
			closeHelpGui();
		} else {
			mousePosX = mouseX -= guiLeft;
			mousePosY = mouseY -= guiTop;

			for (int i = 0; i < 6; i++) {
				if (inField(mouseX, mouseY, 8, 78 + (19 * i), 150,
						96 + (19 * i))
						&& ((i + scrollbarPos) < Gate.NumberGates)) {
					PacketDispatcher.sendToServer(new PacketChangeGate(tileentity, Gate.GateNames[i + scrollbarPos], direction.ordinal()));

					break;
				}
			}

			if (inField(mouseX, mouseY, 38, 7, 58, 27)
					&& tileentity.canBeMovedByMouse(localToBlockDirections[UP
							.toArrayIndex()])) {
				movingPin = UP;
				movingPinOffsetX = mouseX - 38;
				movingPinOffsetY = mouseY - 7;
			} else if (inField(mouseX, mouseY, 38, 47, 58, 67)
					&& tileentity.canBeMovedByMouse(localToBlockDirections[DOWN
							.toArrayIndex()])) {
				movingPin = DOWN;
				movingPinOffsetX = mouseX - 38;
				movingPinOffsetY = mouseY - 47;
			} else if (inField(mouseX, mouseY, 98, 7, 118, 27)
					&& tileentity
							.canBeMovedByMouse(localToBlockDirections[NORTH
									.toArrayIndex()])) {
				movingPin = NORTH;
				movingPinOffsetX = mouseX - 98;
				movingPinOffsetY = mouseY - 7;
			} else if (inField(mouseX, mouseY, 118, 27, 138, 47)
					&& tileentity.canBeMovedByMouse(localToBlockDirections[EAST
							.toArrayIndex()])) {
				movingPin = EAST;
				movingPinOffsetX = mouseX - 118;
				movingPinOffsetY = mouseY - 27;
			} else if (inField(mouseX, mouseY, 98, 47, 118, 67)
					&& tileentity
							.canBeMovedByMouse(localToBlockDirections[SOUTH
									.toArrayIndex()])) {
				movingPin = SOUTH;
				movingPinOffsetX = mouseX - 98;
				movingPinOffsetY = mouseY - 47;
			} else if (inField(mouseX, mouseY, 78, 27, 98, 47)
					&& tileentity.canBeMovedByMouse(localToBlockDirections[WEST
							.toArrayIndex()])) {
				movingPin = WEST;
				movingPinOffsetX = mouseX - 78;
				movingPinOffsetY = mouseY - 27;
			}

			if (inField(mouseX, mouseY, 168, 3, 172, 7)) {
				quit();
			}
		}

		if (nyanCat) {
			stopEasterEgg();
		}
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
		if (!help) {
			mousePosX = mouseX -= guiLeft;
			mousePosY = mouseY -= guiTop;

			if (which == -1) {
				mousePos = -1;
				for (int i = 0; i < 6; i++) {
					if (inField(mouseX, mouseY, 8, 78 + (19 * i), 150,
							96 + (19 * i))) {
						mousePos = (byte) i;

						break;
					}
				}
			} else {
				if (movingPin != null) {
					movingPin = null;
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
			final int spacesToAdd = (stringWidth - fontRendererObj
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

	@Override
	protected void quit() {
		super.quit();

		tileentity.logOut(username);
	}

	private void renderGateFrameAt(int x, int y,
			BrainStoneDirection localToBlockDirections2) {
		if (tileentity.shallRenderPin(localToBlockDirections2)) {
			final byte powerLevel = tileentity
					.getPowerLevel(localToBlockDirections2);
			if (powerLevel == -1) {
				drawTexturedModalRect(x, y, 216, 20, 20, 20);
			} else {
				drawTexturedModalRect(x, y,
						176 + (((powerLevel & 8) >> 1) * 5),
						(powerLevel & 7) * 20, 20, 20);
			}
		}
	}

	private void renderGateLetterAt(int x, int y, BrainStoneDirection direction) {
		if (tileentity.shallRenderPin(direction)) {
			drawString(tileentity.getGateLetter(direction), x, y,
					tileentity.getGateColor(direction), 2.0F);
		}
	}

	private void startEasterEgg() {
		if (!nyanCat) {
			soundHandler.pauseSounds();

			nyanCat = true;

			soundLoop = new SoundLoop();
			soundLoop.start();

			numberStars = (width * height) / starEachPixels;
			renderNyanCat = false;
		}
	}

	private void stopEasterEgg() {
		if (nyanCat) {
			nyanCat = false;

			soundLoop.stopSound();
			soundLoop = null;

			stars = null;
			renderNyanCat = false;

			(new Thread() {
				@Override
				public void run() {
					try {
						sleep(20);
					} catch (final InterruptedException e) {
						BSP.warnException(e);
					}
					soundHandler.resumeSounds();
				}
            }).start();
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

package brainstonemod.client.gui;

import java.io.IOException;

import net.minecraft.util.StatCollector;
import brainstonemod.client.gui.template.GuiBrainStoneBase;
import brainstonemod.common.container.ContainerBlockBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;
import brainstonemod.network.BrainStonePacketHandler;

public class GuiBrainLightSensor extends GuiBrainStoneBase {
	/** The temporary storage of the light level (red bars) */
	private int lightLevel;
	/** The the vertical size of the Classic Gui */
	private static final int xSizeClassic = 128;
	/** The the horizontal size of the Classic Gui */
	private static final int ySizeClassic = 94;
	/** The the vertical size of the Simple Gui */
	private static final int xSizeSimple = 128;
	/** The the horizontal size of the Simple Gui */
	private static final int ySizeSimple = 52;
	/** The temporary storage of the current light level (yellow box) */
	private int curLightLevel;
	/**
	 * The temporary storage of the light direction (whether the light level is
	 * triggered from the right or the left
	 */
	private boolean direction;

	/** The temporary storage of the TileEntity */
	private final TileEntityBlockBrainLightSensor tileentity;

	/**
	 * Saves all given values to the variables. And updates the TileEntity.
	 * 
	 * @param tileentityblockbrainlightsensor
	 *            The TileEntity
	 */
	public GuiBrainLightSensor(
			TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor) {
		super(new ContainerBlockBrainLightSensor());
		tileentity = tileentityblockbrainlightsensor;
		direction = tileentity.getDirection();
		setLightLevel(tileentity.getLightLevel());
		tileentity.GUIopen = true;

		BrainStonePacketHandler.sendUpdateOptions(tileentity);
	}

	/**
	 * Plays the click sound.
	 */
	private void click() {
		// TODO Play sound
		mc.getSoundHandler().playSound(null);
		// mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		String tmp;

		if (tileentity.getState()) {
			this.registerTexture("GuiBrainLightSensorClassic");
			final int x = (width - xSizeClassic) / 2;
			final int y = (height - ySizeClassic) / 2;
			drawTexturedModalRect(x, y, 0, 0, xSizeClassic, ySizeClassic);

			if (direction) {
				drawTexturedModalRect(x + 9, y + 23, 9, 94,
						7 * (lightLevel + 1), 64);
			} else {
				final int j1 = 7 * (16 - lightLevel);
				final int k1 = 112 - j1;
				drawTexturedModalRect(x + 9 + k1, y + 23, 9 + k1, 94, j1, 64);
			}

			curLightLevel = tileentity.getCurLightLevel();
			drawTexturedModalRect(x + (curLightLevel * 7) + 8, y + 22,
					8 + (7 * curLightLevel), 158, 6, 66);
			fontRendererObj.drawString(StatCollector
					.translateToLocal("tile.brainLightSensor.name"), x + 6,
					y + 16, 0x404040);
			fontRendererObj.drawString(
					tmp = StatCollector
							.translateToLocal("gui.brainstone.classic"),
					(x + 32) - (fontRendererObj.getStringWidth(tmp) / 2),
					y + 3, 0x404040);
			fontRendererObj.drawString(
					tmp = StatCollector
							.translateToLocal("gui.brainstone.simple"),
					(x + 96) - (fontRendererObj.getStringWidth(tmp) / 2),
					y + 3, 0x404040);
		} else {
			this.registerTexture("GuiBrainLightSensorSimple");
			final int x = (width - xSizeSimple) / 2;
			final int y = (height - ySizeSimple) / 2;
			drawTexturedModalRect(x, y, 0, 0, xSizeSimple, ySizeSimple);
			drawTexturedModalRect(x + 8, y
					+ ((tileentity.getDirection()) ? 18 : 36), 8, 52, 9, 8);

			fontRendererObj.drawString(
					tmp = StatCollector
							.translateToLocal("gui.brainstone.classic"),
					(x + 32) - (fontRendererObj.getStringWidth(tmp) / 2),
					y + 3, 0x404040);
			fontRendererObj.drawString(
					tmp = StatCollector
							.translateToLocal("gui.brainstone.simple"),
					(x + 96) - (fontRendererObj.getStringWidth(tmp) / 2),
					y + 3, 0x404040);
			fontRendererObj.drawString(StatCollector
					.translateToLocal("gui.brainstone.proportional"), x + 20,
					y + 18, 0x404040);
			fontRendererObj.drawString(
					StatCollector.translateToLocal("gui.brainstone.inverted"),
					x + 20, y + 36, 0x404040);
		}
	}

	/**
	 * Takes two coordinates and checks if they are in a given rectangle
	 * 
	 * @param x
	 *            x-Coordinate to check
	 * @param y
	 *            y-Coordinate to check
	 * @param xmin
	 *            min x-Coordinate
	 * @param ymin
	 *            min y-Coordinate
	 * @param xmax
	 *            max x-Coordinate
	 * @param ymax
	 *            max y-Coordinate
	 * @return true if the coordinates are in the rectangle and false if not
	 */
	private boolean inField(int x, int y, int xmin, int ymin, int xmax, int ymax) {
		return (x >= xmin) && (x <= xmax) && (y >= ymin) && (y <= ymax);
	}

	@Override
	protected void keyTyped(char c, int i) {
		if ((i == 1) || (i == mc.gameSettings.keyBindInventory.getKeyCode())) {
			quit();
		}

		if (tileentity.getState()) {
			if (((i == 200) || (i == 205)) && (lightLevel != 15)) {
				setLightLevel(lightLevel + 1);
			}

			if (((i == 208) || (i == 203)) && (lightLevel != 0)) {
				setLightLevel(lightLevel - 1);
			}

			if ((i == 208) || (i == 203) || (i == 200) || (i == 205)) {
				click();
			}
		} else {
			;
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseButton) {
		if (mouseButton == 0) {
			if (tileentity.getState()) {
				x -= (width - xSizeClassic) / 2;
				y -= (height - ySizeClassic) / 2;

				for (int l = 0; l < 16; l++) {
					final int i1 = 7 * l;
					final int j1 = 2 * l;

					if (inField(x, y, 9 + i1, 53 - j1, 12 + i1, 56 + j1)) {
						click();
						setLightLevel(l);
					}
				}

				if (inField(x, y, 63, 0, 127, 9)) {
					tileentity.changeState();
					try {
						tileentity.update(true);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}

				if (inField(x, y, 120, 13, 124, 17)) {
					quit();
				}
			} else {
				x -= (width - xSizeSimple) / 2;
				y -= (height - ySizeSimple) / 2;

				if (inField(x, y, 0, 0, 63, 9)) {
					tileentity.changeState();
					try {
						tileentity.update(true);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}

				if (inField(x, y, 8, 18, 15, 25)) {
					tileentity.setDirection(true);
				}

				if (inField(x, y, 8, 36, 15, 43)) {
					tileentity.setDirection(false);
				}

				if (inField(x, y, 120, 13, 124, 17)) {
					quit();
				}
			}
		}
	}

	/**
	 * Closes the Gui, plays the click sound and updates the TileEntity
	 */
	private void quit() {
		tileentity.GUIopen = false;
		BrainStonePacketHandler.sendUpdateOptions(tileentity);
		click();
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}

	/**
	 * Sets the light level (red bars), updates the TileEntity ,and snycs it
	 * with the server.
	 * 
	 * @param i
	 *            the level to what the light level is going to be set
	 */
	private void setLightLevel(int i) {
		i &= 0xf;

		if (direction && (i == 15)) {
			direction = false;
			lightLevel = i;
		} else if (!direction && (i == 0)) {
			direction = true;
			lightLevel = i;
		} else {
			lightLevel = i;
		}

		tileentity.setLightLevel(lightLevel);
		tileentity.setDirection(direction);

		BrainStonePacketHandler.sendUpdateOptions(tileentity);
	}
}

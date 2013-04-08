package mods.brainstone.guis;

import mods.brainstone.containers.ContainerBlockBrainLightSensor;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.GuiBrainStoneBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import net.minecraft.util.StatCollector;

public class GuiBrainLightSensor extends GuiBrainStoneBase {
	/** The temporary storage of the light level (red bars) */
	private int lightLevel;
	/** The the vertical size of the Gui */
	private static final int xSize = 128;
	/** The the horizontal size of the Gui */
	private static final int ySize = 94;
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
		this.setLightLevel(tileentity.getLightLevel());
		tileentity.GUIopen = true;

		BrainStonePacketHandler.sendUpdateOptions(tileentity);
	}

	/**
	 * Plays the click sound.
	 */
	private void click() {
		mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		this.registerTexture("GuiBrainLightSensorClassic");
		final int l = (width - xSize) / 2;
		final int i1 = (height - ySize) / 2;
		this.drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		if (direction) {
			this.drawTexturedModalRect(l + 9, i1 + 23, 9, 94,
					7 * (lightLevel + 1), 64);
		} else {
			final int j1 = 7 * (16 - lightLevel);
			final int k1 = 112 - j1;
			this.drawTexturedModalRect(l + 9 + k1, i1 + 23, 9 + k1, 94, j1, 64);
		}

		curLightLevel = tileentity.getCurLightLevel();
		this.drawTexturedModalRect(l + (curLightLevel * 7) + 8, i1 + 22,
				8 + (7 * curLightLevel), 158, 6, 66);
		fontRenderer.drawString(
				StatCollector.translateToLocal("tile.brainLightSensor.name"),
				l + 6, i1 + 16, 0x404040);
		fontRenderer.drawString(
				StatCollector.translateToLocal("gui.brainstone.classic"),
				l + 6, i1 + 3, 0x404040);
		fontRenderer.drawString(
				StatCollector.translateToLocal("gui.brainstone.new"),
				l + 51, i1 + 3, 0x404040);
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
		if ((i == 1) || (i == mc.gameSettings.keyBindInventory.keyCode)) {
			this.quit();
		}

		if (((i == 200) || (i == 205)) && (lightLevel != 15)) {
			this.setLightLevel(lightLevel + 1);
		}

		if (((i == 208) || (i == 203)) && (lightLevel != 0)) {
			this.setLightLevel(lightLevel - 1);
		}

		if ((i == 208) || (i == 203) || (i == 200) || (i == 205)) {
			this.click();
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		if (k == 0) {
			i -= (width - xSize) / 2;
			j -= (height - ySize) / 2;

			for (int l = 0; l < 16; l++) {
				final int i1 = 7 * l;
				final int j1 = 2 * l;

				if (this.inField(i, j, 9 + i1, 53 - j1, 12 + i1, 56 + j1)) {
					this.click();
					this.setLightLevel(l);
				}
			}

			if (this.inField(i, j, 120, 3, 124, 7)) {
				this.quit();
			}
		}
	}

	/**
	 * Closes the Gui, plays the click sound and updates the TileEntity
	 */
	private void quit() {
		tileentity.GUIopen = false;
		BrainStonePacketHandler.sendUpdateOptions(tileentity);
		this.click();
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

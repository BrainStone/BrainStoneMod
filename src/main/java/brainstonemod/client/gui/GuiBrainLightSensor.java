package brainstonemod.client.gui;

import net.minecraft.util.StatCollector;
import brainstonemod.client.gui.template.GuiBrainStoneBase;
import brainstonemod.common.container.ContainerBlockBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;
import brainstonemod.network.BrainStonePacketHelper;

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
	public GuiBrainLightSensor(TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor) {
		super(new ContainerBlockBrainLightSensor(), tileentityblockbrainlightsensor);
		tileentity = tileentityblockbrainlightsensor;
		direction = tileentity.getDirection();
		setLightLevel(tileentity.getLightLevel());

		if (tileentity.getState()) {
			setSize(xSizeClassic, ySizeClassic);
		} else {
			setSize(xSizeSimple, ySizeSimple);
		}
	}

	@Override
	protected void drawGuiBackground(float partialTicks, int mouseX, int mouseY) {
		if (tileentity.getState()) {
			bindTexture("GuiBrainLightSensorClassic");

			drawTexturedModalRect(0, 0, 0, 0, xSizeClassic, ySizeClassic);

			if (direction) {
				drawTexturedModalRect(9, 23, 9, 94, 7 * (lightLevel + 1), 64);
			} else {
				final int j1 = 7 * (16 - lightLevel);
				final int k1 = 112 - j1;
				drawTexturedModalRect(9 + k1, 23, 9 + k1, 94, j1, 64);
			}

			curLightLevel = tileentity.getCurLightLevel();
			drawTexturedModalRect((curLightLevel * 7) + 8, 22,
					8 + (7 * curLightLevel), 158, 6, 66);
			drawString(
					StatCollector
							.translateToLocal("tile.brainLightSensor.name"),
					6, 16, 0x404040);
			drawCenteredString(
					StatCollector.translateToLocal("gui.brainstone.classic"),
					32, 7, 0x404040);
			drawCenteredString(
					StatCollector.translateToLocal("gui.brainstone.simple"),
					96, 7, 0x404040);
		} else {
			bindTexture("GuiBrainLightSensorSimple");

			drawTexturedModalRect(0, 0, 0, 0, xSizeSimple, ySizeSimple);
			drawTexturedModalRect(8, ((tileentity.getDirection()) ? 18 : 36),
					8, 52, 9, 8);

			drawCenteredString(
					StatCollector.translateToLocal("gui.brainstone.classic"),
					32, 7, 0x404040);
			drawCenteredString(
					StatCollector.translateToLocal("gui.brainstone.simple"),
					96, 7, 0x404040);
			drawString(
					StatCollector
							.translateToLocal("gui.brainstone.proportional"),
					20, 18, 0x404040);
			drawString(
					StatCollector.translateToLocal("gui.brainstone.inverted"),
					20, 36, 0x404040);
		}
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
					tileentity.updateEntity();
				}

				if (inField(x, y, 120, 13, 124, 17)) {
					quit();
				}
			} else {
				x -= (width - xSizeSimple) / 2;
				y -= (height - ySizeSimple) / 2;

				if (inField(x, y, 0, 0, 63, 9)) {
					tileentity.changeState();
					tileentity.updateEntity();
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

			if (tileentity.getState()) {
				setSize(xSizeClassic, ySizeClassic);
			} else {
				setSize(xSizeSimple, ySizeSimple);
			}
		}
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

		BrainStonePacketHelper.sendUpdateTileEntityPacket(tileentity);
	}
}

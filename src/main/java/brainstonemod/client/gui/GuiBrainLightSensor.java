package brainstonemod.client.gui;

import brainstonemod.client.gui.template.GuiBrainStoneBase;
import brainstonemod.common.container.ContainerGeneric;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.serverbound.PacketChangeDirection;
import brainstonemod.network.packet.serverbound.PacketChangeState;
import brainstonemod.network.packet.serverbound.PacketLightSensor;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiBrainLightSensor extends GuiBrainStoneBase {
	/** The temporary storage of the light level (red bars) */
	private int lightLevel;
	/** The the vertical size of the Classic Gui */
	private static final int xSizeClassic = 128;
	/** The the horizontal size of the Classic Gui */
	private static final int ySizeClassic = 94;
	/** The temporary storage of the current light level (yellow box) */
	private int curLightLevel;
	/**
	 * The temporary storage of the light direction (whether the light level is
	 * triggered from the right or the left
	 */
	private boolean direction;

	/** The temporary storage of the TileEntity */
	private final TileEntityBrainLightSensor tileentity;

	/**
	 * Saves all given values to the variables. And updates the TileEntity.
	 * 
	 * @param te
	 *            The TileEntity
	 */
	public GuiBrainLightSensor(TileEntityBrainLightSensor te) {
		super(new ContainerGeneric(), te);
		tileentity = te;
		direction = tileentity.getDirection();
		setLightLevel(tileentity.getLightLevel());

		setSize(xSizeClassic, ySizeClassic);
	}

	@Override
	protected void drawGuiBackground(float partialTicks, int mouseX, int mouseY) {
		if (tileentity.isClassic()) {
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
					I18n.format("tile.brainLightSensor.name"),
					6, 16, 0x404040);
			drawCenteredString(
					I18n.format("gui.brainstone.classic"),
					32, 7, 0x404040);
			drawCenteredString(
					I18n.format("gui.brainstone.simple"),
					96, 7, 0x404040);
		} else {
			bindTexture("GuiBrainLightSensorSimple");

			drawTexturedModalRect(0, 0, 0, 0, xSizeClassic, ySizeClassic);
			drawTexturedModalRect(8, ((tileentity.getDirection()) ? 18 : 36),
					8, 94, 9, 8);

			drawCenteredString(
					I18n.format("gui.brainstone.classic"),
					32, 7, 0x404040);
			drawCenteredString(
					I18n.format("gui.brainstone.simple"),
					96, 7, 0x404040);
			drawString(
					I18n.format("gui.brainstone.proportional"),
					20, 18, 0x404040);
			drawString(
					I18n.format("gui.brainstone.inverted"),
					20, 36, 0x404040);
		}
	}

	@Override
	protected void keyTyped(char c, int i) {
		if ((i == Keyboard.KEY_ESCAPE) || (i == mc.gameSettings.keyBindInventory.getKeyCode())) {
			quit();
		}

		if (tileentity.isClassic()) {
			if (((i == Keyboard.KEY_RIGHT) || (i == Keyboard.KEY_UP)) && (lightLevel != 15)) {
				setLightLevel(lightLevel + 1);
			}

			if (((i == Keyboard.KEY_LEFT) || (i == Keyboard.KEY_DOWN)) && (lightLevel != 0)) {
				setLightLevel(lightLevel - 1);
			}

			if ((i == Keyboard.KEY_LEFT) || (i == Keyboard.KEY_DOWN) || (i == Keyboard.KEY_RIGHT) || (i == Keyboard.KEY_UP)) {
				click();
			}
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseButton) {
		if (mouseButton == 0) {
			if (tileentity.isClassic()) {
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
					click();
					PacketDispatcher.sendToServer(new PacketChangeState(tileentity));
				}
			} else {
				x -= (width - xSizeClassic) / 2;
				y -= (height - ySizeClassic) / 2;

				if (inField(x, y, 0, 0, 63, 9)) {
					click();
					PacketDispatcher.sendToServer(new PacketChangeState(tileentity));
				}

				if (inField(x, y, 8, 18, 15, 25)) {
					PacketDispatcher.sendToServer(new PacketChangeDirection(tileentity, true));
				}

				if (inField(x, y, 8, 36, 15, 43)) {
					PacketDispatcher.sendToServer(new PacketChangeDirection(tileentity, false));
				}
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

		PacketDispatcher.sendToServer(new PacketLightSensor(tileentity, lightLevel, direction));
	}
}

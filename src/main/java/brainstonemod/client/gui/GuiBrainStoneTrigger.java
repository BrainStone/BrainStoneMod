package brainstonemod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;

import brainstonemod.BrainStone;
import brainstonemod.client.gui.helper.BrainStoneButton;
import brainstonemod.client.gui.helper.BrainStoneGuiButton;
import brainstonemod.client.gui.template.GuiBrainStoneBase;
import brainstonemod.common.container.ContainerBlockBrainStoneTrigger;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;
import brainstonemod.network.BrainStonePacketHelper;

public class GuiBrainStoneTrigger extends GuiBrainStoneBase {
	private final TileEntityBlockBrainStoneTrigger tileentity;
	private int page, hovered;
	private final int max_page;
	private final String[] Mobs;
	private final BrainStoneGuiButton buttons;

	public GuiBrainStoneTrigger(InventoryPlayer inventoryplayer,
			TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger) {
		super(new ContainerBlockBrainStoneTrigger(inventoryplayer,
				tileentityblockbrainstonetrigger),
				tileentityblockbrainstonetrigger);
		tileentity = tileentityblockbrainstonetrigger;
		Mobs = new String[4];
		page = 0;
		hovered = -1;
		max_page = (BrainStone.getClientSideTiggerEntities().size() / 4)
				+ (((BrainStone.getClientSideTiggerEntities().size() % 4) == 0) ? -1
						: 0);
		buttons = new BrainStoneGuiButton(this);
		buttons.addButton(new BrainStoneButton(0, 10, 32, 131, 9, 194, 9, 204,
				9));
		buttons.addButton(new BrainStoneButton(1, 10, 32, 131, 45, 194, 45,
				204, 45));

		for (int i = 0; i < 4; i++) {
			buttons.addButton(new BrainStoneButton(i + 10, 8, 8, 12,
					12 + (i * 18), 0, 166, 0, 174).changeTriggerBox(7,
					7 + (i * 18), 78, 24 + (i * 18)));

			buttons.addButton(new BrainStoneButton((i * 20) + 30, 10, 10, 78,
					11 + (18 * i), true));
			buttons.addButton(new BrainStoneButton((i * 20) + 31, 10, 10, 118,
					11 + (18 * i), true));

			for (int j = 0; j < 15; j++) {
				buttons.addButton(new BrainStoneButton((i * 20) + 32 + j, 2, 3,
						88 + (2 * j), 13 + (18 * i), true));
			}
		}

		buttons.addButton(new BrainStoneButton(20, 9, 6, 160, 65, 176, 65, 185,
				65));
		buttons.addButton(new BrainStoneButton(21, 9, 6, 160, 73, 176, 73, 185,
				73));

		setMobs();

		int tmp = tileentity.max_delay;

		if (tmp < 1) {
			tmp = 1;
		} else if (tmp > 9) {
			tmp = 9;
		}

		buttons.getButton(21).inactive = (tmp == 1);
		buttons.getButton(20).inactive = (tmp == 9);

		setSize(176, 166);
	}

	public void buttonClicked(int ID) {
		if (ID == 0) {
			page--;
			setMobs();
		} else if (ID == 1) {
			page++;
			setMobs();
		} else if ((ID >= 10) && (ID < 14)) {
			tileentity.invertMobTriggered(Mobs[ID - 10]);
		} else if ((ID >= 20) && (ID < 30)) {
			int tmp = tileentity.max_delay + (((ID * 2) - 41) * -1);

			if (tmp < 1) {
				tmp = 1;
			} else if (tmp > 9) {
				tmp = 9;
			}

			buttons.getButton(21).inactive = (tmp == 1);
			buttons.getButton(20).inactive = (tmp == 9);

			tileentity.max_delay = (byte) tmp;
		} else if (ID >= 30) {
			ID -= 30;
			final int row = ID / 20;
			ID = ID % 20;
			int power;
			final String mob = Mobs[row];

			if (tileentity.getMobTriggered(mob)) {
				power = tileentity.getMobPower(mob);
				if (ID == 0) {
					power--;
				} else if (ID == 1) {
					power++;
				} else {
					power = ID - 1;
				}

				if (power < 1) {
					power = 1;
				} else if (power > 15) {
					power = 15;
				}

				tileentity.setMobTriggered(mob, power);
			}
		}
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiBackground(float partialTicks, int mouseX, int mouseY) {
		if (tileentity == null)
			return;

		drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);

		buttons.render(0, 0);

		String tmp;

		for (int t = 0; t < 4; t++) {
			if (tileentity.getMobTriggered((tmp = Mobs[t]))) {
				drawTexturedModalRect(12, 12 + (18 * t), 8, 166, 10, 7);
				if (tileentity.getMobTriggered(tmp)) {
					drawTexturedModalRect(88, 13 + (18 * t), 88, 172,
							tileentity.getMobPower(tmp) * 2, 6);
				}
			}
		}
	}

	@Override
	protected void drawGuiForeground(int mouseX, int mouseY) {
		if (tileentity == null)
			return;

		String tmp;
		String message;

		for (int i = 0; i < 4; i++) {
			message = StatCollector.translateToLocal(tmp = Mobs[i]);
			
			if(fontRendererObj.getStringWidth(message) > 100) {
				do {
					message = message.substring(0, message.length() - 1);
				} while(fontRendererObj.getStringWidth(message + "...") > 100);
				
				message += "...";
			}
			
			drawString(message, 25,
					12 + (18 * i), tileentity.getMobTriggered(tmp) ? 0xffffff
							: 0x111111);
		}

		drawCenteredString((page + 1) + "/" + (max_page + 1), 156, 17, 0x000000);
		drawString("Delay", 144, 54, 0x000000);
		drawString(String.valueOf(tileentity.max_delay), 148, 68, 0xffffff);

		this.bindTexture();

		if ((hovered != -1)
				&& tileentity.getMobTriggered((tmp = Mobs[hovered]))) {
			drawTexturedModalRect(80, 13 + (18 * hovered), 80, 166, 6, 6);
			drawTexturedModalRect(120, 13 + (18 * hovered), 120, 166, 6, 6);
			drawTexturedModalRect(88, 13 + (18 * hovered), 88, 166,
					tileentity.getMobPower(tmp) * 2, 6);
		}
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() {
		if (Mouse.getEventButton() == -1) {
			mouseMovedOrUp((Mouse.getEventX() * width) / mc.displayWidth,
					height - ((Mouse.getEventY() * height) / mc.displayHeight)
							- 1, Mouse.getEventButton());
		}

		super.handleMouseInput();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char c, int i) {
		super.keyTyped(c, i);

		if ((i == 1) || (i == mc.gameSettings.keyBindInventory.getKeyCode())) {
			quit();
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);

		if (tileentity == null)
			return;

		if (button == 0) {
			x -= guiLeft;
			y -= guiTop;

			buttons.onClick(x, y);

			if (inField(x, y, 167, 4, 171, 8)) {
				quit();
			}

			BrainStonePacketHelper.sendUpdateTileEntityPacket(tileentity);
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@Override
	protected void mouseMovedOrUp(int x, int y, int button) {
		super.mouseMovedOrUp(x, y, button);

		if (button == -1) {
			hovered = -1;

			for (int i = 0; i < 4; i++) {
				if (inField(x - guiLeft, y - guiTop, 7, 7 + (i * 18), 128,
						24 + (i * 18))) {
					hovered = i;
				}
			}
		} else {
			buttons.hover();
		}
	}

	private void setMobs() {
		if (page < 0) {
			page = 0;
		}

		if (page > max_page) {
			page = max_page;
		}

		buttons.getButton(0).inactive = (page == 0);
		buttons.getButton(1).inactive = (page == max_page);

		final int length = BrainStone.getClientSideTiggerEntities().size();
		final String[] keys = BrainStone.getClientSideTiggerEntities().keySet()
				.toArray(new String[length]);
		final int page4 = page * 4;

		if ((page == max_page) && (length != ((max_page - 1) * 4))) {
			int tmp;

			for (int i = 0; i < 4; i++) {
				tmp = i + page4;

				if (tmp < length) {
					Mobs[i] = keys[tmp];
					buttons.getButton(i + 10).inactive = false;
					for (int j = 0; j < 17; j++) {
						buttons.getButton((i * 20) + 30 + j).inactive = false;
					}
				} else {
					Mobs[i] = "";
					buttons.getButton(i + 10).inactive = true;
					for (int j = 0; j < 17; j++) {
						buttons.getButton((i * 20) + 30 + j).inactive = true;
					}
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				Mobs[i] = keys[i + page4];
				buttons.getButton(i + 10).inactive = false;
				for (int j = 0; j < 17; j++) {
					buttons.getButton((i * 20) + 30 + j).inactive = false;
				}
			}
		}
	}
}
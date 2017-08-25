package brainstonemod.client.gui;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;
import brainstonemod.client.gui.helper.BrainStoneButton;
import brainstonemod.client.gui.helper.BrainStoneGuiButton;
import brainstonemod.client.gui.template.GuiBrainStoneBase;
import brainstonemod.common.container.ContainerBrainStoneTrigger;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneSounds;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.serverbound.PacketDisableMobs;
import brainstonemod.network.packet.serverbound.PacketEnableMobs;
import brainstonemod.network.packet.serverbound.PacketInvertMobTriggered;
import brainstonemod.network.packet.serverbound.PacketSetMaxDelay;
import brainstonemod.network.packet.serverbound.PacketSetMobTriggered;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiBrainStoneTrigger extends GuiBrainStoneBase {
	private final TileEntityBrainStoneTrigger tileentity;
	private int page, hovered;
	private final int max_page;
	private final String[] mobs;
	private final BrainStoneGuiButton buttons;

	private static final int catScale = 6;
	private static final int starEachPixels = 1000 * (catScale * catScale);
	private static final long millisPerCatFrame = 100L;
	private final char[] lastChars;
	private SoundLoop soundLoop;
	private boolean nyanCat;
	private boolean renderNyanCat;
	private long lastAnimationProgress;
	private int numberStars;
	private StarAnimation[] stars;
	private final Random nyanrand = new Random();

	public GuiBrainStoneTrigger(InventoryPlayer inventoryplayer, TileEntityBrainStoneTrigger te) {
		super(new ContainerBrainStoneTrigger(inventoryplayer, te), te);
		xSize = 176;
		ySize = 166;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		tileentity = te;

		mobs = new String[4];
		page = 0;
		hovered = -1;
		max_page = (BrainStone.getClientSideTiggerEntities().size() / 4)
				+ (((BrainStone.getClientSideTiggerEntities().size() % 4) == 0) ? -1 : 0);
		buttons = new BrainStoneGuiButton(this);

		lastChars = new char[4];
		soundLoop = null;
		nyanCat = false;
	}

	@Override
	public void initGui() {
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		buttons.addButton(new BrainStoneButton(0, 10, 32, 131, 9, 194, 9, 204, 9));
		buttons.addButton(new BrainStoneButton(1, 10, 32, 131, 45, 194, 45, 204, 45));

		for (int i = 0; i < 4; i++) {
			buttons.addButton(new BrainStoneButton(i + 10, 8, 8, 12, 12 + (i * 18), 0, 166, 0, 174).changeTriggerBox(7,
					7 + (i * 18), 78, 24 + (i * 18)));

			buttons.addButton(new BrainStoneButton((i * 20) + 30, 10, 10, 78, 11 + (18 * i), true));
			buttons.addButton(new BrainStoneButton((i * 20) + 31, 10, 10, 118, 11 + (18 * i), true));

			for (int j = 0; j < 15; j++) {
				buttons.addButton(new BrainStoneButton((i * 20) + 32 + j, 2, 3, 88 + (2 * j), 13 + (18 * i), true));
			}
		}

		buttons.addButton(new BrainStoneButton(20, 9, 6, 160, 65, 176, 65, 185, 65));
		buttons.addButton(new BrainStoneButton(21, 9, 6, 160, 73, 176, 73, 185, 73));

		setMobs();

		int tmp = tileentity.getMaxDelay();

		if (tmp < 1) {
			tmp = 1;
		} else if (tmp > 9) {
			tmp = 9;
		}

		buttons.getButton(21).inactive = (tmp == 1);
		buttons.getButton(20).inactive = (tmp == 9);

		super.initGui();
	}

	public void buttonClicked(int ID) {
		if (ID == 0) {
			page--;
			setMobs();
		} else if (ID == 1) {
			page++;
			setMobs();
		} else if ((ID >= 10) && (ID < 14)) {
			PacketDispatcher.sendToServer(new PacketInvertMobTriggered(tileentity, mobs[ID - 10]));
		} else if ((ID >= 20) && (ID < 30)) {
			int tmp = tileentity.getMaxDelay() + (((ID * 2) - 41) * -1);

			if (tmp < 1) {
				tmp = 1;
			} else if (tmp > 9) {
				tmp = 9;
			}

			buttons.getButton(21).inactive = (tmp == 1);
			buttons.getButton(20).inactive = (tmp == 9);

			PacketDispatcher.sendToServer(new PacketSetMaxDelay(tileentity, (byte) tmp));
		} else if (ID >= 30) {
			ID -= 30;
			final int row = ID / 20;
			ID = ID % 20;
			int power;
			final String mob = mobs[row];

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

				PacketDispatcher.sendToServer(new PacketSetMobTriggered(tileentity, mob, power));
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
		hovered = -1;

		for (int i = 0; i < 4; i++) {
			if (inField(mouseX - guiLeft, mouseY - guiTop, 7, 7 + (i * 18), 128, 24 + (i * 18))) {
				hovered = i;
			}

			if (tileentity.getMobTriggered((tmp = mobs[i]))) {
				drawTexturedModalRect(12, 12 + (18 * i), 8, 166, 10, 7);

				if (tileentity.getMobTriggered(tmp)) {
					drawTexturedModalRect(88, 13 + (18 * i), 88, 172, tileentity.getMobPower(tmp) * 2, 6);
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
			message = I18n.format(tmp = mobs[i]);

			if (fontRenderer.getStringWidth(message) > 100) {
				do {
					message = message.substring(0, message.length() - 1);
				} while (fontRenderer.getStringWidth(message + "...") > 100);

				message += "...";
			}

			drawString(message, 25, 12 + (18 * i), tileentity.getMobTriggered(tmp) ? 0xffffff : 0x111111);
		}

		drawCenteredString(I18n.format("trigger.all"), 153, 13, 0x000000);
		drawCenteredString((page + 1) + "/" + (max_page + 1), 156, 27, 0x000000);
		drawString(I18n.format("trigger.delay"), 144, 54, 0x000000);
		drawString(String.valueOf(tileentity.getMaxDelay()), 148, 68, 0xffffff);

		this.bindTexture();

		if ((hovered != -1) && tileentity.getMobTriggered((tmp = mobs[hovered]))) {
			drawTexturedModalRect(80, 13 + (18 * hovered), 80, 166, 6, 6);
			drawTexturedModalRect(120, 13 + (18 * hovered), 120, 166, 6, 6);
			drawTexturedModalRect(88, 13 + (18 * hovered), 88, 166, tileentity.getMobPower(tmp) * 2, 6);
		}

		if (renderNyanCat) {
			drawNyanCat();
		}
	}

	@Override
	protected void keyTyped(char letter, int key) throws IOException {
		if (nyanCat) {
			stopEasterEgg();

			// Just stop the easter egg. Do nothing else.
			return;
		}

		// Closes the GUI when ESC or Inventory key
		super.keyTyped(letter, key);

		// Shift array by one
		System.arraycopy(lastChars, 0, lastChars, 1, lastChars.length - 1);
		lastChars[0] = letter;

		if (((lastChars[2] == 'l') && (lastChars[1] == 'o') && (lastChars[0] == 'l'))
				|| ((lastChars[3] == 'a') && (lastChars[2] == 's') && (lastChars[1] == 'd') && (lastChars[0] == 'f'))) {
			startEasterEgg();
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);

		if (tileentity == null)
			return;

		if (button == 0) {
			x -= guiLeft;
			y -= guiTop;

			buttons.onClick(x, y);

			if (inField(x, y, 162, 3, 169, 10)) {
				PacketDispatcher.sendToServer(new PacketEnableMobs(tileentity));
				click();
			} else if (inField(x, y, 162, 12, 169, 19)) {
				PacketDispatcher.sendToServer(new PacketDisableMobs(tileentity));
				click();
			}
		}

		if (nyanCat) {
			stopEasterEgg();
		}
	}

	@Override
	protected void mouseClickMove(int x, int y, int button, long timeSinceClick) {
		super.mouseClickMove(x, y, button, timeSinceClick);

		if (button != -1) {
			buttons.hover();
		}
	}

	// TODO: rewrite this so that new stars get added randomly not when an old
	// one disappears
	private void drawNyanCat() {
		// Transforming the rendering space
		zLevel = 101F;
		bindTexture(getTextureBaseName() + "_easter_egg");
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
		drawTexturedModalRect(-23, -9, 41, ((int) ((lastAnimationProgress / 2) % 2L)) * 19, 16, 18);
		drawTexturedModalRect(-23, -9, 41, ((int) ((lastAnimationProgress / 2) % 2L)) * 19, 14, 19);

		for (int i = 1; i < (((width / (catScale * 2)) / 16) + 1); i++) {
			drawTexturedModalRect((i * -16) - 23, -9, 41, ((int) ((lastAnimationProgress / 2) % 2L)) * 19, 16, 19);
		}

		// Render the cat
		drawTexturedModalRect(-17, -10, 0, ((int) (lastAnimationProgress % 12L)) * 21, 34, 21);

		for (final StarAnimation star : stars) {
			star.render();
		}

		// Progress the animation
		if ((System.currentTimeMillis() / millisPerCatFrame) > lastAnimationProgress) {
			lastAnimationProgress = System.currentTimeMillis() / millisPerCatFrame;

			for (int i = 0; i < stars.length; i++) {
				if (!stars[i].shift()) {
					stars[i] = new StarAnimation();
				}
			}
		}

		// Reset the render space
		zLevel = 0F;
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
		final String[] keys = BrainStone.getClientSideTiggerEntities().keySet().toArray(new String[length]);
		final int page4 = page * 4;

		if ((page == max_page) && (length != ((max_page - 1) * 4))) {
			int tmp;

			for (int i = 0; i < 4; i++) {
				tmp = i + page4;

				if (tmp < length) {
					mobs[i] = keys[tmp];
					buttons.getButton(i + 10).inactive = false;
					for (int j = 0; j < 17; j++) {
						buttons.getButton((i * 20) + 30 + j).inactive = false;
					}
				} else {
					mobs[i] = "";
					buttons.getButton(i + 10).inactive = true;
					for (int j = 0; j < 17; j++) {
						buttons.getButton((i * 20) + 30 + j).inactive = true;
					}
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				mobs[i] = keys[i + page4];
				buttons.getButton(i + 10).inactive = false;
				for (int j = 0; j < 17; j++) {
					buttons.getButton((i * 20) + 30 + j).inactive = false;
				}
			}
		}
	}

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
				currentSound = playSoundAtClient(BrainStoneSounds.nyan_intro);
				sleep(4037);

				if (run) {
					renderNyanCat = true;
					lastAnimationProgress = System.currentTimeMillis() / millisPerCatFrame;
				}

				while (run) {
					currentSound = playSoundAtClient(BrainStoneSounds.nyan_loop);
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
			y = nyanrand.nextInt(height / catScale) - (height / (catScale * 2));

			frame = nyanrand.nextInt(6);
		}

		public void ranomizeX() {
			x = nyanrand.nextInt(width / catScale) - (width / (catScale * 2));
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
}

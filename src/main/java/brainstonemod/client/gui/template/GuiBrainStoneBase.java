package brainstonemod.client.gui.template;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import brainstonemod.network.BrainStonePacketHelper;

public abstract class GuiBrainStoneBase extends GuiContainer {
	private class Sound implements ISound {
		private final float pitch;
		private final boolean repeat;
		private final int repeatDelay;
		private final ResourceLocation resourceLocation;
		private final float volume;

		public Sound(String sound, float volume, float pitch) {
			resourceLocation = new ResourceLocation(sound);
			this.volume = volume;
			this.pitch = pitch;

			repeat = false;
			repeatDelay = 0;
		}

		public Sound(String sound, float volume, float pitch, int repeatDelay) {
			resourceLocation = new ResourceLocation(sound);
			this.volume = volume;
			this.pitch = pitch;
			this.repeatDelay = repeatDelay;

			repeat = true;
		}

		@Override
		public boolean canRepeat() {
			return repeat;
		}

		@Override
		public AttenuationType getAttenuationType() {
			return AttenuationType.NONE;
		}

		@Override
		public float getPitch() {
			return pitch;
		}

		@Override
		public ResourceLocation getPositionedSoundLocation() {
			return resourceLocation;
		}

		@Override
		public int getRepeatDelay() {
			return repeatDelay;
		}

		@Override
		public float getVolume() {
			return volume;
		}

		@Override
		public float getXPosF() {
			return tileentity.xCoord;
		}

		@Override
		public float getYPosF() {
			return tileentity.yCoord;
		}

		@Override
		public float getZPosF() {
			return tileentity.zCoord;
		}
	}

	protected SoundHandler soundHandler;
	protected TileEntityBrainStoneSyncBase tileentity;
	protected boolean textureLoaded;

	public GuiBrainStoneBase(Container par1Container,
			TileEntityBrainStoneSyncBase tileentity) {
		super(par1Container);

		this.tileentity = tileentity;

		soundHandler = Minecraft.getMinecraft().getSoundHandler();
	}

	/**
	 * Binds a texture with the name of the current class.
	 */
	protected void bindTexture() {
		this.bindTexture(this.getClass().getSimpleName());
	}

	/**
	 * Binds the specified texture into the GUI.<br>
	 * 
	 * @param resource
	 *            The texture to be bound
	 */
	protected void bindTexture(ResourceLocation resource) {
		textureLoaded = true;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(resource);
	}

	/**
	 * Binds the specified texture into the GUI.<br>
	 * The texture is located in the "gui" directory of the brainstonemod-assest
	 * directory.
	 * 
	 * @param name
	 *            The texture to be bound
	 */
	protected void bindTexture(String name) {
		bindTexture(new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
				BrainStone.guiPath + name + ".png"));
	}

	/**
	 * Plays the click sound.
	 */
	protected void click() {
		playSoundAtClient("random.click");
	}

	protected void drawCenteredString(String text, float x, float y) {
		drawCenteredString(text, x, y, 0x000000);
	}

	protected void drawCenteredString(String text, float x, float y, float scale) {
		drawCenteredString(text, x, y, 0x000000, scale);
	}

	protected void drawCenteredString(String text, float x, float y, int color) {
		drawCenteredString(text, x, y, color, 1.0f);
	}

	protected void drawCenteredString(String text, float x, float y, int color,
			float scale) {
		drawString(
				text,
				x - (fontRendererObj.getStringWidth(text) / 2.0f),
				y
						- (text.split("\r\n|\r|\n").length * (fontRendererObj.FONT_HEIGHT / 2)),
				color, scale);
	}

	@Override
	public final void drawDefaultBackground() {
		super.drawDefaultBackground();
	}

	protected abstract void drawGuiBackground(float partialTicks, int mouseX,
			int mouseY);

	@Override
	protected final void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		final int xSizeOld = xSize;
		final int ySizeOld = ySize;

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0.0f);

		textureLoaded = false;
		drawGuiBackground(partialTicks, mouseX, mouseY);

		GL11.glPopMatrix();

		xSize = xSizeOld;
		ySize = ySizeOld;

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	@Override
	protected final void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final int xSizeOld = xSize;
		final int ySizeOld = ySize;

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		GL11.glPushMatrix();
		GL11.glTranslatef(-guiLeft, -guiTop, 0.0f);
		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0.0f);

		drawGuiForeground(mouseX, mouseY);

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		xSize = xSizeOld;
		ySize = ySizeOld;

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	protected void drawGuiForeground(int mouseX, int mouseY) {
	}

	protected final void drawMyDefaultBackground() {
		GL11.glPopMatrix();

		drawDefaultBackground();

		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0.0f);
	}

	protected void drawString(String text, float x, float y) {
		drawString(text, x, y, 0x000000);
	}

	protected void drawString(String text, float x, float y, float scale) {
		drawString(text, x, y, 0x000000, scale);
	}

	protected void drawString(String text, float x, float y, int color) {
		drawString(text, x, y, color, 1.0f);
	}

	protected void drawString(String text, float x, float y, int color,
			float scale) {
		GL11.glPushMatrix();
		prepareMatrices(x, y, scale);

		fontRendererObj.drawString(text, 0, 0, color);

		GL11.glPopMatrix();
	}

	@Override
	public void drawTexturedModalRect(int x, int y, int u, int v, int width,
			int height) {
		if (!textureLoaded) {
			bindTexture();
		}

		super.drawTexturedModalRect(x, y, u, v, width, height);
	}

	protected void drawTexturedModalRect(int x, int y, int u, int v, int width,
			int height, boolean inverted) {
		if (inverted) {
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

			drawTexturedModalRect(-x - width, -y - height, u, v, width, height);

			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		} else {
			drawTexturedModalRect(x, y, u, v, width, height);
		}
	}

	@Override
	public void drawTexturedModelRectFromIcon(int x, int y, IIcon icon,
			int width, int height) {
		if (!textureLoaded) {
			bindTexture();
		}

		super.drawTexturedModelRectFromIcon(x, y, icon, width, height);
	}

	/**
	 * Takes two coordinates and checks if they are in a given rectangle
	 * 
	 * @param x
	 *            x-Coordinate to check
	 * @param y
	 *            y-Coordinate to check
	 * @param xMin
	 *            min x-Coordinate
	 * @param yMin
	 *            min y-Coordinate
	 * @param xMax
	 *            max x-Coordinate
	 * @param yMax
	 *            max y-Coordinate
	 * @return true if the coordinates are in the rectangle and false if not
	 */
	protected boolean inField(int x, int y, int xMin, int yMin, int xMax,
			int yMax) {
		return (x >= xMin) && (x <= xMax) && (y >= yMin) && (y <= yMax);
	}

	protected ISound playSoundAtClient(ISound sound) {
		soundHandler.playSound(sound);

		return sound;
	}

	protected ISound playSoundAtClient(String sound) {
		return playSoundAtClient(sound, 1.0F, 1.0F);
	}

	protected ISound playSoundAtClient(String sound, float volume, float pitch) {
		final ISound iSound = new Sound(sound, volume, pitch);

		return playSoundAtClient(iSound);
	}

	protected ISound playSoundAtClient(String sound, float volume, float pitch,
			int repeatDelay) {
		final ISound iSound = new Sound(sound, volume, pitch, repeatDelay);

		return playSoundAtClient(iSound);
	}

	protected ISound playSoundAtClient(String sound, int repeatDelay) {
		return playSoundAtClient(sound, 1.0F, 1.0F, repeatDelay);
	}

	private void prepareMatrices(float x, float y, float scale) {
		GL11.glTranslatef(x, y, 0.0F);
		GL11.glScalef(scale, scale, scale);
	}

	/**
	 * Closes the Gui, plays the click sound and updates the TileEntity
	 */
	protected void quit() {
		click();
		mc.thePlayer.closeScreen();

		BrainStonePacketHelper.sendUpdateTileEntityPacket(tileentity);
	}

	protected void setSize(int width, int height) {
		xSize = width;
		ySize = height;
	}

	protected void setTempSize(int width, int height) {
		GL11.glPopMatrix();

		xSize = width;
		ySize = height;

		guiLeft = (this.width - xSize) / 2;
		guiTop = (this.height - ySize) / 2;

		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0.0f);
	}

	protected ISound stopSoundAtClient(ISound sound) {
		soundHandler.stopSound(sound);

		return sound;
	}
}
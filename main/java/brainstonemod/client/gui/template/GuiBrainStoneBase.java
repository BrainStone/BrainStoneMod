package brainstonemod.client.gui.template;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import brainstonemod.network.BrainStonePacketHelper;

public abstract class GuiBrainStoneBase extends GuiContainer {
	protected TileEntityBrainStoneSyncBase tileentity;

	public GuiBrainStoneBase(Container par1Container,
			TileEntityBrainStoneSyncBase tileentity) {
		super(par1Container);

		this.tileentity = tileentity;
	}

	/**
	 * Binds a texture with the name of the current class.
	 */
	public void bindTexture() {
		this.bindTexture(this.getClass().getSimpleName());
	}

	/**
	 * Binds the specified texture into the GUI.<br>
	 * The texture is located in the "gui" directory of the brainstonemod-assest
	 * directory.
	 * 
	 * @param Name
	 *            The texture to be bound
	 */
	public void bindTexture(String Name) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(
				new ResourceLocation("brainstonemod:" + BrainStone.guiPath
						+ Name + ".png"));
	}

	/**
	 * Plays the click sound.
	 */
	protected void click() {
		tileentity.getWorldObj().playSound(tileentity.xCoord,
				tileentity.yCoord, tileentity.zCoord, "random.click", 1.0F,
				1.0F, true);
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

		bindTexture();
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

	public final void drawMyDefaultBackground() {
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

	private void prepareMatrices(float x, float y, float scale) {
		GL11.glTranslatef(x, y, 0.0F);
		GL11.glScalef(scale, scale, scale);
	}

	/**
	 * Closes the Gui, plays the click sound and updates the TileEntity
	 */
	protected void quit() {
		click();
		mc.displayGuiScreen(null);
		mc.setIngameFocus();

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
}
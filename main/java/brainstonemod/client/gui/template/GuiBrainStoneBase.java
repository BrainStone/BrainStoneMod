package brainstonemod.client.gui.template;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;

public abstract class GuiBrainStoneBase extends GuiContainer {

	protected TileEntity tileentity;

	public GuiBrainStoneBase(Container par1Container) {
		super(par1Container);
	}

	public void registerTexture() {
		this.registerTexture(this.getClass().getSimpleName());
	}

	public void registerTexture(String Name) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(
				new ResourceLocation("brainstonemod:" + BrainStone.guiPath
						+ Name + ".png"));
	}
	
	/**
	 * Plays the click sound.
	 */
	protected void click() {
		tileentity.getWorldObj().playSound(tileentity.xCoord, tileentity.yCoord,
				tileentity.zCoord, "random.click", 1.0F, 1.0F, true);
	}
}
package brainstonemod.client.gui.template;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;

public abstract class GuiBrainStoneBase extends GuiContainer {

	protected TileEntity tileentity;

	public GuiBrainStoneBase(Container par1Container, TileEntity tileentity) {
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
}
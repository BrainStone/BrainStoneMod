package brainstonemod.templates;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;

public abstract class GuiBrainStoneBase extends GuiContainer {

	public GuiBrainStoneBase(Container par1Container) {
		super(par1Container);
	}

	public void registerTexture() {
		this.registerTexture(this.getClass().getSimpleName());
	}

	public void registerTexture(String Name) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.func_110577_a(new ResourceLocation("brainstonemod:"
				+ BrainStone.guiPath + Name + ".png"));
	}
}
package mods.brainstonemod.templates;

import mods.brainstonemod.BrainStone;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

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
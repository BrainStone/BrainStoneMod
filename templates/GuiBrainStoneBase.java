package mods.brainstone.templates;

import ayl;
import bgf;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import tj;

public abstract class GuiBrainStoneBase extends ayl
{
  public GuiBrainStoneBase(tj par1Container)
  {
    super(par1Container);
  }

  public void registerTexture() {
    registerTexture(getClass().getSimpleName());
  }

  public void registerTexture(String Name) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    this.f.p.b("/mods/brainstone/textures/gui/" + Name + ".png");
  }
}
package mods.brainstone.templates;

import ayf;
import bfy;
import mods.brainstone.BrainStone;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import td;

public abstract class GuiBrainStoneBase extends ayf
{
  public GuiBrainStoneBase(td par1Container)
  {
    super(par1Container);
  }

  public void registerTexture() {
    registerTexture(getClass().getSimpleName());
  }

  public void registerTexture(String Name) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    this.f.p.b(BrainStone.guiPath + Name + ".png");
  }
}
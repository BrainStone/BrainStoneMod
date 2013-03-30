package mods.brainstone;

import aab;
import bdw;
import net.minecraft.client.Minecraft;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy
{
  public Minecraft getClient()
  {
    return null;
  }

  public aab getClientWorld()
  {
    return null;
  }

  public bdw getPlayer()
  {
    return null;
  }

  public void registerOre() {
    OreDictionary.registerOre("brainstoneore", BrainStone.brainStoneOre());
  }

  public void registerRenderInformation()
  {
  }

  public void registerTextures()
  {
  }
}
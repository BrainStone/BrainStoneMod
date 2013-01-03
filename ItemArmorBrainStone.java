package brainstone;

import amq;
import net.minecraftforge.common.IArmorTextureProvider;
import su;
import sv;
import up;
import ur;

public class ItemArmorBrainStone extends su
  implements IArmorTextureProvider
{
  public ItemArmorBrainStone(int i, sv armorMaterial, int par3, int par4)
  {
    super(BrainStone.getId(359 + i), armorMaterial, par3, par4);
  }

  public String getArmorTextureFile(ur itemstack)
  {
    if (itemstack.c == BrainStone.brainStoneLeggings().cj)
    {
      return "/brainstone/BrainStoneTextures/brainstone_armor_2.png";
    }

    return "/brainstone/BrainStoneTextures/brainstone_armor_1.png";
  }

  public String getTextureFile()
  {
    return "/brainstone/BrainStoneTextures/textures.png";
  }

  public boolean a(ur tool, ur material)
  {
    return material.c == BrainStone.brainStone().cm;
  }
}
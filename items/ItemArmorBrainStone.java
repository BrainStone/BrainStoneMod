package mods.brainstone.items;

import apa;
import ly;
import mods.brainstone.BrainStone;
import net.minecraftforge.common.IArmorTextureProvider;
import uo;
import uq;
import wk;
import wm;

public class ItemArmorBrainStone extends uo
  implements IArmorTextureProvider
{
  public ItemArmorBrainStone(int i, uq armorMaterial, int par3, int par4)
  {
    super(BrainStone.getId(359 + i), armorMaterial, par3, par4);
  }

  public String getArmorTextureFile(wm itemstack)
  {
    if (itemstack.c == BrainStone.brainStoneLeggings().cp) {
      return "/mods/brainstone/textures/armor/brainstone_armor_2.png";
    }
    return "/mods/brainstone/textures/armor/brainstone_armor_1.png";
  }

  public boolean a(wm tool, wm material)
  {
    return material.c == BrainStone.brainStone().cz;
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}
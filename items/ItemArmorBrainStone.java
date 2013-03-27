package mods.brainstone.items;

import aou;
import ly;
import mods.brainstone.BrainStone;
import net.minecraftforge.common.IArmorTextureProvider;
import ui;
import uk;
import we;
import wg;

public class ItemArmorBrainStone extends ui
  implements IArmorTextureProvider
{
  public ItemArmorBrainStone(int i, uk armorMaterial, int par3, int par4)
  {
    super(BrainStone.getId(359 + i), armorMaterial, par3, par4);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }

  public String getArmorTextureFile(wg itemstack)
  {
    if (itemstack.c == BrainStone.brainStoneLeggings().cp) {
      return BrainStone.armorPath + "/brainstone_armor_2.png";
    }
    return BrainStone.armorPath + "/brainstone_armor_1.png";
  }

  public boolean a(wg tool, wg material)
  {
    return material.c == BrainStone.brainStone().cz;
  }
}
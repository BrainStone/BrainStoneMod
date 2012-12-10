package net.braintonemod.src;

import akc;
import amj;
import amn;
import qx;
import uk;
import um;
import xv;

public class ItemHoeBrainStone extends uk
{
  public ItemHoeBrainStone(int i, EnumToolMaterialBrainStone enumtoolmaterialbrainstone)
  {
    super(BrainStone.getId(359 + i));
    this.ch = 1;
    e(enumtoolmaterialbrainstone.getMaxUses());
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
  }

  public boolean onItemUse(um itemstack, qx entityplayer, xv world, int i, int j, int k, int l)
  {
    int i1 = world.a(i, j, k);
    int j1 = world.a(i, j + 1, k);

    if (((l != 0) && (j1 == 0) && (i1 == amj.x.cm)) || (i1 == amj.y.cm))
    {
      amj block = amj.aD;
      world.a(i + 0.5F, j + 0.5F, k + 0.5F, block.cz.e(), (block.cz.c() + 1.0F) / 2.0F, block.cz.d() * 0.8F);

      if (world.J)
      {
        return true;
      }

      world.e(i, j, k, block.cm);
      itemstack.a(1, entityplayer);
      return true;
    }

    return false;
  }

  public boolean n_()
  {
    return true;
  }
}
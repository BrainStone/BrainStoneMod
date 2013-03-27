package mods.brainstone.blocks;

import aae;
import ahz;
import aou;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BlockBrainStoneContainerBase;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneHiders;
import uy;
import wg;

public abstract class BlockBrainStoneHiders extends BlockBrainStoneContainerBase
{
  public BlockBrainStoneHiders(int i)
  {
    super(BrainStone.getId(i), ahz.e);

    a(uy.d);
  }

  public boolean f()
  {
    return true;
  }

  public int c(aae iblockaccess, int i, int j, int k)
  {
    TileEntityBlockBrainStoneHiders tileentityblockbrainstonehiders = (TileEntityBlockBrainStoneHiders)iblockaccess.r(i, j, k);

    if (tileentityblockbrainstonehiders == null) {
      return 16777215;
    }
    wg itemstack = tileentityblockbrainstonehiders.a(0);

    if (itemstack == null) {
      return 16777215;
    }
    int l = itemstack.c;

    if ((l >= aou.r.length) || (l < 0)) {
      return 16777215;
    }
    aou block = aou.r[l];

    if (block == null) {
      return 16777215;
    }
    return aou.r[l].c(iblockaccess, i, j, k);
  }
}
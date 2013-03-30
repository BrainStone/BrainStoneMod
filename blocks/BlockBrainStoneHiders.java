package mods.brainstone.blocks;

import aak;
import aif;
import apa;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BlockBrainStoneContainerBase;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneHiders;
import ve;
import wm;

public abstract class BlockBrainStoneHiders extends BlockBrainStoneContainerBase
{
  public BlockBrainStoneHiders(int i)
  {
    super(BrainStone.getId(i), aif.e);

    a(ve.d);
  }

  public boolean f()
  {
    return true;
  }

  public int c(aak iblockaccess, int i, int j, int k)
  {
    TileEntityBlockBrainStoneHiders tileentityblockbrainstonehiders = (TileEntityBlockBrainStoneHiders)iblockaccess.r(i, j, k);

    if (tileentityblockbrainstonehiders == null) {
      return 16777215;
    }
    wm itemstack = tileentityblockbrainstonehiders.a(0);

    if (itemstack == null) {
      return 16777215;
    }
    int l = itemstack.c;

    if ((l >= apa.r.length) || (l < 0)) {
      return 16777215;
    }
    apa block = apa.r[l];

    if (block == null) {
      return 16777215;
    }
    return apa.r[l].c(iblockaccess, i, j, k);
  }
}
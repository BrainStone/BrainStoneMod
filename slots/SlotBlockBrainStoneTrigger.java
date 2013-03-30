package mods.brainstone.slots;

import ana;
import apa;
import mods.brainstone.BrainStone;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import ul;
import wm;
import xn;

public class SlotBlockBrainStoneTrigger extends ul
{
  public static boolean staticIsItemValid(wm itemstack)
  {
    if (!(itemstack.b() instanceof xn)) {
      return false;
    }
    int i = itemstack.c;

    if ((i > apa.r.length) || (i <= 0)) {
      return false;
    }
    apa block = apa.r[i];
    int j = block.cz;

    if (block == null) {
      return false;
    }
    if ((j == BrainStone.brainStoneTrigger().cz) || (j == apa.O.cz))
    {
      return false;
    }
    return block.c();
  }

  public SlotBlockBrainStoneTrigger(TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger, int i, int j, int k)
  {
    super(tileentityblockbrainstonetrigger, i, j, k);
  }

  public int a()
  {
    return 1;
  }

  public boolean a(wm itemstack)
  {
    return staticIsItemValid(itemstack);
  }
}
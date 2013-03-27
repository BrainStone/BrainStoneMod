package mods.brainstone.slots;

import amu;
import aou;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BSP;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import uf;
import wg;

public class SlotBlockBrainStoneTrigger extends uf
{
  public SlotBlockBrainStoneTrigger(TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger, int i, int j, int k)
  {
    super(tileentityblockbrainstonetrigger, i, j, k);
  }

  public int a()
  {
    return 1;
  }

  public boolean a(wg itemstack)
  {
    int i = itemstack.c;

    if ((i > aou.r.length) || (i <= 0)) {
      BSP.debugOnly_println("Out of Range");

      return false;
    }

    aou block = aou.r[i];
    int j = block.cz;

    if (block == null) {
      BSP.debugOnly_println("Block is null");

      return false;
    }

    if ((j == BrainStone.brainStoneTrigger().cz) || (j == aou.O.cz))
    {
      BSP.debugOnly_println("Wrong Block");

      return false;
    }
    if (!block.c())
      BSP.debugOnly_println("Not opaque");
    else {
      BSP.debugOnly_println("TRUe!");
    }

    return block.c();
  }
}
package net.braintonemod.src;

import akm;
import amj;
import sq;
import um;

class SlotBlockBrainStoneTrigger extends sq
{
  public SlotBlockBrainStoneTrigger(TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger, int i, int j, int k)
  {
    super(tileentityblockbrainstonetrigger, i, j, k);
  }

  public boolean a(um itemstack)
  {
    int i = itemstack.c;

    if ((i > amj.p.length) || (i <= 0))
    {
      return false;
    }

    amj block = amj.p[i];
    int j = block.cm;

    if (block == null)
    {
      return false;
    }

    if ((j == BrainStone.brainStoneTrigger().cm) || (j == amj.N.cm))
    {
      return false;
    }

    return block.c();
  }

  public int a()
  {
    return 1;
  }
}
package brainstone;

import akt;
import amq;
import sr;
import ur;

class SlotBlockBrainStoneTrigger extends sr
{
  public SlotBlockBrainStoneTrigger(TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger, int i, int j, int k)
  {
    super(tileentityblockbrainstonetrigger, i, j, k);
  }

  public boolean a(ur itemstack)
  {
    int i = itemstack.c;

    if ((i > amq.p.length) || (i <= 0))
    {
      return false;
    }

    amq block = amq.p[i];
    int j = block.cm;

    if (block == null)
    {
      return false;
    }

    if ((j == BrainStone.brainStoneTrigger().cm) || (j == amq.N.cm) || (!BrainStone.isTextureInList(block.getTextureFile())))
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
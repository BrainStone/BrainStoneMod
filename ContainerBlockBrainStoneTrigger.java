package brainstone;

import qw;
import qx;
import rq;
import sr;

public class ContainerBlockBrainStoneTrigger extends rq
{
  private TileEntityBlockBrainStoneTrigger trigger;

  public ContainerBlockBrainStoneTrigger(qw inventoryplayer, TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger)
  {
    this.trigger = tileentityblockbrainstonetrigger;
    a(new SlotBlockBrainStoneTrigger(this.trigger, 0, 148, 29));

    for (int i = 0; i < 3; i++)
    {
      for (int k = 0; k < 9; k++)
      {
        a(new sr(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
      }
    }

    for (int j = 0; j < 9; j++)
    {
      a(new sr(inventoryplayer, j, 8 + j * 18, 142));
    }
  }

  public boolean a(qx entityplayer)
  {
    return this.trigger.a_(entityplayer);
  }
}
package net.braintonemod.src;

import qw;
import qx;
import rp;
import sq;

public class ContainerBlockBrainStoneTrigger extends rp
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
        a(new sq(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
      }
    }

    for (int j = 0; j < 9; j++)
    {
      a(new sq(inventoryplayer, j, 8 + j * 18, 142));
    }
  }

  public boolean a(qx entityplayer)
  {
    return this.trigger.a_(entityplayer);
  }
}
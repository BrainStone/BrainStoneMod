package mods.brainstone.containers;

import mods.brainstone.slots.SlotBlockBrainStoneTrigger;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import si;
import sk;
import td;
import uf;

public class ContainerBlockBrainStoneTrigger extends td
{
  private final TileEntityBlockBrainStoneTrigger trigger;

  public ContainerBlockBrainStoneTrigger(si inventoryplayer, TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger)
  {
    this.trigger = tileentityblockbrainstonetrigger;

    a(new SlotBlockBrainStoneTrigger(this.trigger, 0, 148, 29));

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new uf(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }

    }

    for (i = 0; i < 9; i++)
      a(new uf(inventoryplayer, i, 8 + i * 18, 142));
  }

  public boolean a(sk entityplayer)
  {
    return this.trigger.a(entityplayer);
  }
}
package mods.brainstone.handlers;

import cpw.mods.fml.common.IPickupNotifier;
import mods.brainstone.BrainStone;
import rb;
import sk;
import we;
import wg;

public class BrainStonePickupNotifier
  implements IPickupNotifier
{
  public void notifyPickup(rb item, sk player)
  {
    if (item.d().c == BrainStone.brainStoneDust().cp)
      player.a(BrainStone.WTHIT(), 1);
  }
}
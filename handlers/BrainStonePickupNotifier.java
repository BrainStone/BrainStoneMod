package mods.brainstone.handlers;

import cpw.mods.fml.common.IPickupNotifier;
import mods.brainstone.BrainStone;
import rh;
import sq;
import wk;
import wm;

public class BrainStonePickupNotifier
  implements IPickupNotifier
{
  public void notifyPickup(rh item, sq player)
  {
    if (item.d().c == BrainStone.brainStoneDust().cp)
      player.a(BrainStone.WTHIT(), 1);
  }
}
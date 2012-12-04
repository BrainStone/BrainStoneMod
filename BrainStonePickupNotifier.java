package net.braintonemod.src;

import cpw.mods.fml.common.IPickupNotifier;
import px;
import qx;
import uk;
import um;

public class BrainStonePickupNotifier
  implements IPickupNotifier
{
  public void notifyPickup(px item, qx player)
  {
    if (item.a.c == BrainStone.brainStoneDust().cg)
    {
      player.a(BrainStone.WTHIT(), 1);
    }
  }
}
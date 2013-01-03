package brainstone;

import cpw.mods.fml.common.IPickupNotifier;
import px;
import qx;
import up;
import ur;

public class BrainStonePickupNotifier
  implements IPickupNotifier
{
  public void notifyPickup(px item, qx player)
  {
    if (item.d().c == BrainStone.brainStoneDust().cj)
    {
      player.a(BrainStone.WTHIT(), 1);
    }
  }
}
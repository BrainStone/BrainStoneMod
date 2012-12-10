package net.braintonemod.src;

import cpw.mods.fml.common.IFuelHandler;
import uk;
import um;

public class BrainStoneFuelHandler
  implements IFuelHandler
{
  public int getBurnTime(um fuel)
  {
    int id = fuel.c;

    if (id == BrainStone.coalBriquette().cg)
    {
      return 16000;
    }

    return 0;
  }
}
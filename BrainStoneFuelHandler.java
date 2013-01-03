package brainstone;

import cpw.mods.fml.common.IFuelHandler;
import up;
import ur;

public class BrainStoneFuelHandler
  implements IFuelHandler
{
  public int getBurnTime(ur fuel)
  {
    int id = fuel.c;

    if (id == BrainStone.coalBriquette().cj)
    {
      return 16000;
    }

    return 0;
  }
}
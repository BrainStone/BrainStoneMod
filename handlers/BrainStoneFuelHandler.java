package mods.brainstone.handlers;

import cpw.mods.fml.common.IFuelHandler;
import mods.brainstone.BrainStone;
import we;
import wg;

public class BrainStoneFuelHandler
  implements IFuelHandler
{
  public int getBurnTime(wg fuel)
  {
    int id = fuel.c;

    if (id == BrainStone.coalBriquette().cp) {
      return 16000;
    }
    return 0;
  }
}
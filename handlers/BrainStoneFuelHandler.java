package mods.brainstone.handlers;

import cpw.mods.fml.common.IFuelHandler;
import mods.brainstone.BrainStone;
import wk;
import wm;

public class BrainStoneFuelHandler
  implements IFuelHandler
{
  public int getBurnTime(wm fuel)
  {
    int id = fuel.c;

    if (id == BrainStone.coalBriquette().cp) {
      return 16000;
    }
    return 0;
  }
}
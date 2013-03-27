package mods.brainstone.handlers;

import aou;
import cpw.mods.fml.common.ICraftingHandler;
import lt;
import mods.brainstone.BrainStone;
import sk;
import we;
import wg;

public class BrainStoneCraftingHandler
  implements ICraftingHandler
{
  public void onCrafting(sk player, wg item, lt craftMatrix)
  {
    int i = item.c;

    if ((i == BrainStone.brainLightSensor().cz) || (i == BrainStone.brainStoneTrigger().cz))
    {
      player.a(BrainStone.intelligentBlocks(), 1);
    } else if ((i == BrainStone.brainStoneSword().cp) || (i == BrainStone.brainStoneShovel().cp) || (i == BrainStone.brainStonePickaxe().cp) || (i == BrainStone.brainStoneAxe().cp) || (i == BrainStone.brainStoneHoe().cp))
    {
      player.a(BrainStone.intelligentTools(), 1);
    } else if (i == BrainStone.brainLogicBlock().cz)
      player.a(BrainStone.logicBlock(), 1);
  }

  public void onSmelting(sk player, wg item)
  {
    if (item.c == BrainStone.brainStone().cz)
      player.a(BrainStone.itLives(), 1);
  }
}
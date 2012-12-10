package net.braintonemod.src;

import amj;
import cpw.mods.fml.common.ICraftingHandler;
import la;
import qx;
import uk;
import um;

public class BrainStoneCraftingHandler
  implements ICraftingHandler
{
  public void onCrafting(qx player, um item, la craftMatrix)
  {
    int i = item.c;

    if ((i == BrainStone.brainLightSensor().cm) || (i == BrainStone.brainStoneTrigger().cm))
    {
      player.a(BrainStone.intelligentBlocks(), 1);
    }
    else if ((i == BrainStone.brainStoneSword().cg) || (i == BrainStone.brainStoneShovel().cg) || (i == BrainStone.brainStonePickaxe().cg) || (i == BrainStone.brainStoneAxe().cg) || (i == BrainStone.brainStoneHoe().cg))
    {
      player.a(BrainStone.intelligentTools(), 1);
    }
    else if (i == BrainStone.brainLogicBlock().cm)
    {
      player.a(BrainStone.logicBlock(), 1);
    }
  }

  public void onSmelting(qx player, um item)
  {
    if (item.c == BrainStone.brainStone().cm)
    {
      player.a(BrainStone.itLives(), 1);
    }
  }
}
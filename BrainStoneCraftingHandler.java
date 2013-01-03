package brainstone;

import amq;
import cpw.mods.fml.common.ICraftingHandler;
import la;
import qx;
import up;
import ur;

public class BrainStoneCraftingHandler
  implements ICraftingHandler
{
  public void onCrafting(qx player, ur item, la craftMatrix)
  {
    int i = item.c;

    if ((i == BrainStone.brainLightSensor().cm) || (i == BrainStone.brainStoneTrigger().cm))
    {
      player.a(BrainStone.intelligentBlocks(), 1);
    }
    else if ((i == BrainStone.brainStoneSword().cj) || (i == BrainStone.brainStoneShovel().cj) || (i == BrainStone.brainStonePickaxe().cj) || (i == BrainStone.brainStoneAxe().cj) || (i == BrainStone.brainStoneHoe().cj))
    {
      player.a(BrainStone.intelligentTools(), 1);
    }
    else if (i == BrainStone.brainLogicBlock().cm)
    {
      player.a(BrainStone.logicBlock(), 1);
    }
  }

  public void onSmelting(qx player, ur item)
  {
    if (item.c == BrainStone.brainStone().cm)
    {
      player.a(BrainStone.itLives(), 1);
    }
  }
}
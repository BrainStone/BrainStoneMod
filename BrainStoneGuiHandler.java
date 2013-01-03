package brainstone;

import cpw.mods.fml.common.network.IGuiHandler;
import qx;
import yc;

public class BrainStoneGuiHandler
  implements IGuiHandler
{
  public Object getServerGuiElement(int ID, qx player, yc world, int x, int y, int z)
  {
    switch (ID)
    {
    case 0:
    case 2:
      return new ContainerBlockBrainLightSensor();
    case 1:
      return new ContainerBlockBrainStoneTrigger(player.bJ, (TileEntityBlockBrainStoneTrigger)world.q(x, y, z));
    }

    return null;
  }

  public Object getClientGuiElement(int ID, qx player, yc world, int x, int y, int z)
  {
    TileEntityBrainStoneSyncBase tileentity = (TileEntityBrainStoneSyncBase)world.q(x, y, z);

    switch (ID)
    {
    case 0:
      return new GuiBrainLightSensor((TileEntityBlockBrainLightSensor)tileentity);
    case 1:
      return new GuiBrainStoneTrigger(player.bJ, (TileEntityBlockBrainStoneTrigger)tileentity);
    case 2:
      return new GuiBrainLogicBlock((TileEntityBlockBrainLogicBlock)tileentity);
    }

    return null;
  }
}
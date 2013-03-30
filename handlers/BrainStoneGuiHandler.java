package mods.brainstone.handlers;

import aab;
import cpw.mods.fml.common.network.IGuiHandler;
import mods.brainstone.containers.ContainerBlockBrainLightSensor;
import mods.brainstone.containers.ContainerBlockBrainStoneTrigger;
import mods.brainstone.guis.GuiBrainLightSensor;
import mods.brainstone.guis.GuiBrainLogicBlock;
import mods.brainstone.guis.GuiBrainStoneTrigger;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import sq;

public class BrainStoneGuiHandler
  implements IGuiHandler
{
  public Object getClientGuiElement(int ID, sq player, aab world, int x, int y, int z)
  {
    TileEntityBrainStoneSyncBase tileentity = (TileEntityBrainStoneSyncBase)world.r(x, y, z);

    switch (ID) {
    case 0:
      return new GuiBrainLightSensor((TileEntityBlockBrainLightSensor)tileentity);
    case 1:
      return new GuiBrainStoneTrigger(player.bK, (TileEntityBlockBrainStoneTrigger)tileentity);
    case 2:
      return new GuiBrainLogicBlock((TileEntityBlockBrainLogicBlock)tileentity);
    }

    return null;
  }

  public Object getServerGuiElement(int ID, sq player, aab world, int x, int y, int z)
  {
    switch (ID) {
    case 0:
    case 2:
      return new ContainerBlockBrainLightSensor();
    case 1:
      return new ContainerBlockBrainStoneTrigger(player.bK, (TileEntityBlockBrainStoneTrigger)world.r(x, y, z));
    }

    return null;
  }
}
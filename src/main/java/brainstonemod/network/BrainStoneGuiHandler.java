package brainstonemod.network;

import brainstonemod.client.gui.GuiBrainLightSensor;
import brainstonemod.client.gui.GuiBrainStoneAnvil;
import brainstonemod.client.gui.GuiBrainStoneTrigger;
import brainstonemod.common.container.ContainerBrainStoneAnvil;
import brainstonemod.common.container.ContainerBrainStoneTrigger;
import brainstonemod.common.container.ContainerGeneric;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BrainStoneGuiHandler implements IGuiHandler {
  public static final int ID_BRAIN_LIGHT_SENSOR = 0;
  public static final int ID_BRAIN_STONE_TRIGGER = 1;
  public static final int ID_BRAIN_STONE_ANVIL = 2;

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos pos = new BlockPos(x, y, z);
    TileEntity entity = world.getTileEntity(pos);

    switch (ID) {
      case ID_BRAIN_LIGHT_SENSOR:
        return new ContainerGeneric();
      case ID_BRAIN_STONE_TRIGGER:
        if (entity instanceof TileEntityBrainStoneTrigger)
          return new ContainerBrainStoneTrigger(
              player.inventory, (TileEntityBrainStoneTrigger) entity);
      case ID_BRAIN_STONE_ANVIL:
        return new ContainerBrainStoneAnvil(player.inventory, world, pos, player);
      default:
        return null;
    }
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

    switch (ID) {
      case ID_BRAIN_LIGHT_SENSOR:
        if (entity instanceof TileEntityBrainLightSensor)
          return new GuiBrainLightSensor((TileEntityBrainLightSensor) entity);
        else return null;
      case ID_BRAIN_STONE_TRIGGER:
        if (entity instanceof TileEntityBrainStoneTrigger)
          return new GuiBrainStoneTrigger(player.inventory, (TileEntityBrainStoneTrigger) entity);
        else return null;
      case ID_BRAIN_STONE_ANVIL:
        return new GuiBrainStoneAnvil(player.inventory, world);
      default:
        return null;
    }
  }
}

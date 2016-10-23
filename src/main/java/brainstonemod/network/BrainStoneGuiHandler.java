package brainstonemod.network;

import brainstonemod.client.gui.GuiBrainLightSensor;
import brainstonemod.client.gui.GuiBrainStoneTrigger;
import brainstonemod.common.container.ContainerBrainStoneTrigger;
import brainstonemod.common.container.ContainerGeneric;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BrainStoneGuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity != null)
			switch (ID) {
			case 0:
				return new ContainerGeneric();
			case 1:
				if (entity instanceof TileEntityBrainStoneTrigger)
					return new ContainerBrainStoneTrigger(player.inventory, (TileEntityBrainStoneTrigger) entity);
			}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);

		if (entity != null)
			switch (ID) {
			case 0:
				if (entity instanceof TileEntityBrainLightSensor)
					return new GuiBrainLightSensor((TileEntityBrainLightSensor) entity);
				else
					return null;
			case 1:
				if (entity instanceof TileEntityBrainStoneTrigger)
					return new GuiBrainStoneTrigger(player.inventory, (TileEntityBrainStoneTrigger) entity);
				else
					return null;
			default:
				return null;
			}

		return null;
	}
}
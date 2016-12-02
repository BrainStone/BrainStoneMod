package brainstonemod.network;

import brainstonemod.client.gui.GuiBrainLightSensor;
import brainstonemod.client.gui.GuiBrainStoneTrigger;
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
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		if (entity != null) {
			switch (ID) {
			case 0:
				return new ContainerGeneric();
			case 1:
				if (entity instanceof TileEntityBrainStoneTrigger)
					return new ContainerBrainStoneTrigger(player.inventory, (TileEntityBrainStoneTrigger) entity);
			default:
				return null;
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

		if (entity != null) {
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
		}

		return null;
	}
}
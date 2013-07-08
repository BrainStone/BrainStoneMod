package brainstonemod.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import brainstonemod.containers.ContainerBlockBrainLightSensor;
import brainstonemod.containers.ContainerBlockBrainStoneTrigger;
import brainstonemod.guis.GuiBrainLightSensor;
import brainstonemod.guis.GuiBrainLogicBlock;
import brainstonemod.guis.GuiBrainStoneTrigger;
import brainstonemod.templates.TileEntityBrainStoneSyncBase;
import brainstonemod.tileentities.TileEntityBlockBrainLightSensor;
import brainstonemod.tileentities.TileEntityBlockBrainLogicBlock;
import brainstonemod.tileentities.TileEntityBlockBrainStoneTrigger;
import cpw.mods.fml.common.network.IGuiHandler;

public class BrainStoneGuiHandler implements IGuiHandler {
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		final TileEntityBrainStoneSyncBase tileentity = (TileEntityBrainStoneSyncBase) world
				.getBlockTileEntity(x, y, z);

		switch (ID) {
		case 0:
			return new GuiBrainLightSensor(
					(TileEntityBlockBrainLightSensor) tileentity);
		case 1:
			return new GuiBrainStoneTrigger(player.inventory,
					(TileEntityBlockBrainStoneTrigger) tileentity);
		case 2:
			return new GuiBrainLogicBlock(
					(TileEntityBlockBrainLogicBlock) tileentity);
		}

		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case 0:
		case 2:
			return new ContainerBlockBrainLightSensor();
		case 1:
			return new ContainerBlockBrainStoneTrigger(player.inventory,
					(TileEntityBlockBrainStoneTrigger) world
							.getBlockTileEntity(x, y, z));
		}

		return null;
	}
}
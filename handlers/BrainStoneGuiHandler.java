package mods.brainstone.handlers;

import mods.brainstone.containers.ContainerBlockBrainLightSensor;
import mods.brainstone.containers.ContainerBlockBrainStoneTrigger;
import mods.brainstone.guis.GuiBrainLightSensor;
import mods.brainstone.guis.GuiBrainLogicBlock;
import mods.brainstone.guis.GuiBrainStoneTrigger;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
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
package brainstonemod.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import brainstonemod.client.gui.GuiBrainLightSensor;
import brainstonemod.client.gui.GuiBrainLogicBlock;
import brainstonemod.client.gui.GuiBrainStoneTrigger;
import brainstonemod.common.container.ContainerBlockBrainLightSensor;
import brainstonemod.common.container.ContainerBlockBrainStoneTrigger;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
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
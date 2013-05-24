package mods.brainstone.handlers;

import java.util.EnumSet;

import mods.brainstone.BrainStone;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

public class BrainStoneTickHandler implements IScheduledTickHandler {
	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (!BrainStone.called_onPlayerJoin
					&& (FMLClientHandler.instance().getClient().thePlayer != null)
					&& (FMLClientHandler.instance().getClient().thePlayer.worldObj != null)) {
				BrainStone.onPlayerJoin();
				BrainStone.called_onPlayerJoin = true;
			} else if ((FMLClientHandler.instance().getClient().thePlayer == null)
					|| (FMLClientHandler.instance().getClient().thePlayer.worldObj == null)) {
				BrainStone.called_onPlayerJoin = false;
			}
		}
	}
}
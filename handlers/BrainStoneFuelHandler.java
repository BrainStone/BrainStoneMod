package mods.brainstone.handlers;

import mods.brainstone.BrainStone;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class BrainStoneFuelHandler implements IFuelHandler {
	/**
	 * Calculates the fuel time of an itemstack.
	 * 
	 * @param fuel
	 *            the item that is the fuel
	 * @return The time in ticks, that a fuel will burn
	 */
	@Override
	public int getBurnTime(ItemStack fuel) {
		final int id = fuel.itemID;

		if (id == BrainStone.coalBriquette().itemID)
			return 16000; // = 80 Items

		return 0;
	}
}
package brainstonemod.common.api.enderio;

import crazypants.enderio.EnderIO;
import net.minecraft.item.ItemStack;

public final class EnderIOItems {
	private EnderIOItems() {
	}

	public static final ItemStack getSentientEnder() {
		return new ItemStack(EnderIO.itemFrankenSkull, 1, 4);
	}

	public static final ItemStack getXPRod() {
		return new ItemStack(EnderIO.itemXpTransfer, 1, 0);
	}

	public static final ItemStack getOctadicCapacitor() {
		return new ItemStack(EnderIO.itemBasicCapacitor, 1, 2);
	}
}

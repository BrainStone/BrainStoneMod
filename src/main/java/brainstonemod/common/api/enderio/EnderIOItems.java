package brainstonemod.common.api.enderio;

import crazypants.enderio.ModObject;
import net.minecraft.item.ItemStack;

public final class EnderIOItems {
	private EnderIOItems() {
	}

	public static final ItemStack getSentientEnder() {
		return new ItemStack(ModObject.itemFrankenSkull.getItem(), 1, 4);
	}

	public static final ItemStack getXPRod() {
		return new ItemStack(ModObject.itemXpTransfer.getItem(), 1, 0);
	}

	public static final ItemStack getOctadicCapacitor() {
		return new ItemStack(ModObject.itemBasicCapacitor.getItem(), 1, 2);
	}
}

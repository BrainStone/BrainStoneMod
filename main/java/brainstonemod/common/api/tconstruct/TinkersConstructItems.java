package brainstonemod.common.api.tconstruct;

import net.minecraft.item.ItemStack;
import tconstruct.armor.TinkerArmor;

public final class TinkersConstructItems {
	private TinkersConstructItems() {
	}

	public static ItemStack getGreenHeartCanister() {
		return new ItemStack(TinkerArmor.heartCanister, 1, 6);
	}
}

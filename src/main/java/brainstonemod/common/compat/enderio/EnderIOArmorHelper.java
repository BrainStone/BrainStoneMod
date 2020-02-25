package brainstonemod.common.compat.enderio;

import brainstonemod.common.compat.enderio.dark_steel_upgrade.BrainStoneUpgrade;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class EnderIOArmorHelper {
	public static boolean isProtected(ItemStack stack) {
		return BrainStoneUpgrade.UPGRADE.hasUpgrade(stack);
	}
}

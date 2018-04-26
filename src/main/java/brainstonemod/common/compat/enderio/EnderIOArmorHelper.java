package brainstonemod.common.compat.enderio;

import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class EnderIOArmorHelper {
	public static boolean isProtected(ItemStack stack) {
		return BrainStoneUpgrade.UPGRADE.hasUpgrade(stack);
	}
}

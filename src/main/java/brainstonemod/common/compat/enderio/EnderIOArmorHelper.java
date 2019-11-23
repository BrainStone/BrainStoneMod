package brainstonemod.common.compat.enderio;

import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class EnderIOArmorHelper {
	public static boolean isProtected(ItemStack stack) {
		return BrainStoneUpgrade.HELMET.hasUpgrade(stack) || BrainStoneUpgrade.CHEST.hasUpgrade(stack)
				|| BrainStoneUpgrade.LEGS.hasUpgrade(stack) || BrainStoneUpgrade.BOOTS.hasUpgrade(stack);
	}
}

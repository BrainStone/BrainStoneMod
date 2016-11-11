package brainstonemod.common.compat.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;

public class ImmersiveEngineeringItems {
	public static ItemStack getSlag() {
		return new ItemStack(IEContent.itemMaterial, 1, 7);
	}
}

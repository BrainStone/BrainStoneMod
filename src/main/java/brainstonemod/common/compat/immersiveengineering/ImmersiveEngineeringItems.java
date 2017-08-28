package brainstonemod.common.compat.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class ImmersiveEngineeringItems {
	public static ItemStack getSlag() {
		return new ItemStack(IEContent.itemMaterial, 1, 7);
	}

	public static ItemStack getHVCapacitor() {
		return new ItemStack(IEContent.blockMetalDevice0, 1, 2);
	}
}

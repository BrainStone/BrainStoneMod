package brainstonemod.common.api.extrautilities;

import com.rwtema.extrautils.ExtraUtils;

import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class ExtraUtilsItems {
	public static ItemStack getBecrockiumIngot() {
		return new ItemStack(ExtraUtils.bedrockium);
	}

	public static ItemStack getReinforcedWateringCan() {
		return new ItemStack(ExtraUtils.wateringCan, 1, 3);
	}
}

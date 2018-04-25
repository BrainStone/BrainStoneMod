package brainstonemod.common.compat.forestry;

import forestry.core.ModuleCore;
import forestry.core.items.EnumElectronTube;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class ForestryItems {
	public static ItemStack getEmeraldElectronTube() {
		return ModuleCore.items.tubes.get(EnumElectronTube.EMERALD, 1);
	}
}

package brainstonemod.common.compat.forestry;

import forestry.core.PluginCore;
import forestry.core.items.EnumElectronTube;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class ForestryItems {
	public static ItemStack getEmeraldElectronTube() {
		return PluginCore.items.tubes.get(EnumElectronTube.EMERALD, 1);
	}
}

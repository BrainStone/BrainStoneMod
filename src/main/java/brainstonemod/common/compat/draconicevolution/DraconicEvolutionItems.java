package brainstonemod.common.compat.draconicevolution;

import com.brandon3055.draconicevolution.DEFeatures;

import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public final class DraconicEvolutionItems {
	public static ItemStack getDragonHeart() {
		return new ItemStack(DEFeatures.dragonHeart);
	}

	public static ItemStack getWyvernFluxCapacitor() {
		return DEFeatures.wyvernCapacitor;
	}
}

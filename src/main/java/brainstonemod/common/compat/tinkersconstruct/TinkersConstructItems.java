package brainstonemod.common.compat.tinkersconstruct;

import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

@UtilityClass
public class TinkersConstructItems {
	public static ItemStack getManyullyToughRod() {
		return TinkerTools.toughToolRod.getItemstackWithMaterial(TinkerMaterials.manyullyn);
	}

	public static ItemStack getMendingMoss() {
		return TinkerCommons.matMendingMoss;
	}
}

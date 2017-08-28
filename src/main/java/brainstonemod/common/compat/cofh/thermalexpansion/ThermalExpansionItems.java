package brainstonemod.common.compat.cofh.thermalexpansion;

import cofh.thermalexpansion.item.ItemCapacitor;
import cofh.thermalfoundation.item.ItemMaterial;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class ThermalExpansionItems {
	public static ItemStack getSlag() {
		return ItemMaterial.crystalSlag;
	}
	
	public static ItemStack getResonantFluxCapacitor() {
		return ItemCapacitor.capacitorResonant;
	}
}

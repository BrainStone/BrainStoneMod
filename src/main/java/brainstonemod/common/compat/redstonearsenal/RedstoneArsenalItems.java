package brainstonemod.common.compat.redstonearsenal;

import cofh.redstonearsenal.item.ItemMaterial;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class RedstoneArsenalItems {
  public static ItemStack getFluxInfusedObsidianRod() {
    return ItemMaterial.rodObsidianFlux;
  }

  public static ItemStack getFluxedArmorPlating() {
    return ItemMaterial.plateArmorFlux;
  }
}
